/*
 * Copyright 2021-22 Ontology Engineering Group, Universidad Politecnica de Madrid, Spain
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Author: Daniel Garijo and Maria Poveda
 */

package fair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import entities.Check;
import entities.Ontology;

public class FOOPS {
    private static final Logger logger = LoggerFactory.getLogger(FOOPS.class);

    private ArrayList<Check> checks;
    private Path tmpFolder;
    private Ontology ontology;

    public FOOPS(String o, boolean isFromFile){
        boolean custom = false;
        Benchmark benchmark = new Benchmark();
        tmpFolder = null;
        try {
            tmpFolder = Files.createTempDirectory(Path.of("."), "foops");
            logger.info("Temp Directory created");
        }catch(Exception e){
            logger.error("Could not create temporary folder. Exiting");
            return;
        }
        this.ontology = new Ontology(o, isFromFile, tmpFolder);
        if(!isFromFile ){
            logger.info("uri Checks");
            logger.info("ontology: " + ontology);
            logger.info("o: " + o);
            checks =  benchmark.uriChecks(ontology,o);
            logger.info("CHECKS: " + checks.toString());
        }
        else if (isFromFile) {
            checks = benchmark.fileChecks(ontology);
        }
        else if (custom) {
            //Example
            String[] customChecks = {"f1","rdf1","om1"};

        }
    }


    /**
     * Method for passing all the checks.
     */
    public void fairTest(){
        try {
            checks.forEach(Check::check);
        }catch(Exception e){
            logger.error("Error with check");
        }

    }

    /**
    * In construction
    * Method for getting test by name.
    * @param name Name test. 
    * @return ????????
    */
    public Optional<Map<String, String>> getTestByName(String name) {
        
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        // Intentando encontrar el test entre la lista de checks cargados previamente
        for (Check check : checks) {
            // Convertir el check a JSON y luego a JsonObject
            String jsonCheck = gson.toJson(check);
            JsonObject jsonObject = JsonParser.parseString(jsonCheck).getAsJsonObject();
    
            // Comparar la abreviatura
            if (jsonObject.get("abbreviation").getAsString().equals(name)) {
                logger.info("Encontrado");
                // Si coincide, construir el mapa con la información del test
                Map<String, String> details = new HashMap<>();
                details.put("description", jsonObject.get("description").getAsString());
                details.put("id", jsonObject.get("id").getAsString());
                details.put("title", jsonObject.get("title").getAsString());
    
                // Devolver como Optional
                return Optional.of(details);
            }
        }
    
        // Si no se encuentra, devolver Optional vacío
        return Optional.empty();
  
        // return checks.stream()
        //         .map(check -> {
        //             String jsonCheck = gson.toJson(check);
        //             JsonObject jsonObject = JsonParser.parseString(jsonCheck).getAsJsonObject();
        //             return jsonObject;
        //         })
        //         .filter(jsonObject -> jsonObject.get("abbreviation").getAsString().equals(name))
        //         .map(jsonObject -> {
        //             Map<String, String> details = new HashMap<>();
        //             details.put("description", jsonObject.get("description").getAsString());
        //             details.put("id", jsonObject.get("id").getAsString());
        //             details.put("title", jsonObject.get("title").getAsString());
        //             return details;
        //         })
        //         .findFirst();
    }
    /**
    * In construction
    * Method for getting benchmark by name.
    * @param name Name benchmark. 
    * @return ????????
    */
    public Optional<Check> getBenchmarkByName(String name) {
        logger.info("------ Get benchmark -----");
        // Gson gson = new GsonBuilder().
        // excludeFieldsWithoutExposeAnnotation().
        // setPrettyPrinting().
        // create();
        
        // checks.forEach(check -> { 
        //     logger.info("Objeto completo del check: " + check);       
        //     String jsonCheck = gson.toJson(check);
        //     logger.info("JSON completo del check: " + jsonCheck);   
        // });
        return checks.stream()
                    .peek(check -> logger.info("Verifying check: " + check.getClass().getSimpleName()))
                    .filter(check -> check.getClass().getSimpleName().equals(name))
                    .findFirst();
    }
    
    private float getTotalScore(){
        float totalNum = 0;
        for (Check check : checks) {
            totalNum += (float)check.getTotal_passed_tests()/(float)check.getTotal_tests_run();
        }
        //we divide among the number of checks, as some checks do more tests than others but all
        // are equally important
        return totalNum/checks.size();
    }

    /**
     * This method writes the results as a JSON file
     */
    public String exportJSON(){
        String license, title;
        if (this.ontology.getLicense()!=null && !"".equals(ontology.getLicense())){
            license = "\"ontology_license\": \""+this.ontology.getLicense()+"\",\n";
        }else{
            license = "\"ontology_license\": \"unknown\",\n";
        }
        if (this.ontology.getTitle()!=null && !"".equals(ontology.getTitle())){
            title = "\"ontology_title\": \""+this.ontology.getTitle()+"\",\n";
        }else{
            title = "\"ontology_title\": \"unknown\",\n";
        }
        String out = "{\n\"ontology_URI\": \""+this.ontology.getOntologyURI()+"\",\n" +
                title +
                license +
                "\"overall_score\":"+this.getTotalScore()+",\n" +
                "\"checks\":";
        Gson gson = new GsonBuilder().
                excludeFieldsWithoutExposeAnnotation().
                setPrettyPrinting().
                create();
        String jsonChecks = gson.toJson(checks);
        out += jsonChecks +"\n}";
//        System.out.println(out);
        return out;
    }

    /**
     * This function will export a summary of the ontology metadata we were able to find.
     * The metadata is normalized (i.e., equivalent properties may be used). Normalization
     * is made according to MOD 2.0 (subsetting the vocabulary)
     * @param outPath: path where the summary will be written to.
     */
    public void exportNormalizedMetadataToRDF(String outPath){
        // Create a mini model where the main entity is the ontology to describe
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            logger.info("Saving ontology metadata summary to file ");
            IRI ontoURI = IRI.create(this.ontology.getOntologyURI());

            OWLOntology onto = manager.createOntology(ontoURI);
            OWLDataFactory df = manager.getOWLDataFactory();
            // add annotations for each of the properties for which we have a value.
            // all elements get normalized with the selected metadata below
            if (this.ontology.getDescription()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_SCHEMA_DESCRIPTION,ontology.getDescription());
            }
            if(this.ontology.getTitle()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_SCHEMA_NAME,ontology.getTitle());
            }
            if(this.ontology.getName()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_RDFS_LABEL,ontology.getName());
            }
            if(this.ontology.getSupportedMetadata().contains(Constants.FOOPS_NS_URI)){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_VANN_URI, ontology.getOntologyURI());
            }
            if(this.ontology.getVersionIRI()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_OWL_VERSION_IRI, ontology.getVersionIRI());
            }
            if(this.ontology.getVersionInfo()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_OWL_VERSION_INFO, ontology.getVersionInfo());
            }
            if(this.ontology.getNamespacePrefix()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_VANN_PREFIX, ontology.getNamespacePrefix());
            }
            if(this.ontology.getLicense()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_SCHEMA_LICENSE, ontology.getLicense());
            }
            if(this.ontology.getRights()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_DC_RIGHTS, ontology.getRights());
            }
            if(this.ontology.getStatus()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_BIBO_STATUS, ontology.getStatus());
            }
            if(this.ontology.getPreviousVersion()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_OWL_PRIOR_VERSION, ontology.getPreviousVersion());
            }
            if(this.ontology.getCreationDate()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_SCHEMA_DATE_CREATED, ontology.getCreationDate());
            }
            if(this.ontology.getModifiedDate()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_SCHEMA_DATE_MODIFIED, ontology.getModifiedDate());
            }
            if(this.ontology.getIssuedDate()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_DCTERMS_ISSUED, ontology.getIssuedDate());
            }
            if(this.ontology.getSource()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_DCTERMS_SOURCE, ontology.getSource());
            }
            if(this.ontology.getBackwardCompatibility()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_OWL_BACKWARDS_COMPATIBLE, ontology.getBackwardCompatibility());
            }
            if(this.ontology.getCitation()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_SCHEMA_CITATION, ontology.getCitation());
            }
            if(this.ontology.getDoi()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_BIBO_DOI, ontology.getDoi());
            }
            if(this.ontology.getLogo()!=null){
                Utils.addAnnotationToOntology(df,onto,ontoURI,Constants.PROP_SCHEMA_SCHEMA_LOGO, ontology.getLogo());
            }
            if(this.ontology.getAuthors()!=null && !this.ontology.getAuthors().isEmpty()){
                for(String author: this.ontology.getAuthors()) {
                    Utils.addAnnotationToOntology(df, onto, ontoURI, Constants.PROP_SCHEMA_CREATOR, author);
                }
            }
            if(this.ontology.getContributors()!=null && !this.ontology.getContributors().isEmpty()){
                for(String author: this.ontology.getContributors()) {
                    Utils.addAnnotationToOntology(df, onto, ontoURI, Constants.PROP_SCHEMA_CONTRIBUTOR, author);
                }
            }

            try (FileOutputStream stream = new FileOutputStream(outPath)) {
                onto.saveOntology(new TurtleDocumentFormat(),stream);
            } catch (IOException | OWLOntologyStorageException e) {
                logger.error("Error saving the ontology metadata summary to output file");
            }

        }
        catch(Exception e){
            logger.error("Error when producing the summary " + e.getMessage());
        }


    }

    public void removeTemporaryFolders(){
        try {
            FileUtils.deleteDirectory(new File(this.tmpFolder.toString()));
        }catch(Exception e){
            logger.error("Could not delete tmp folder");
        }
    }


    public static void main(String[] args){
        logger.info("\n\n--FAIR ontologies: validation tests--\n");
        // get the arguments
        String ontology = "", outPath = "";
        boolean isFromFile = false;
        int i = 0;
        while (i < args.length) {
            String s = args[i];
            switch (s) {
                case "-ontFile":
                    ontology = args[i + 1];
                    isFromFile = true;
                    i++;
                    break;
                case "-ontURI":
                    ontology = args[i + 1];
                    i++;
                    break;
                case "-out":
                    outPath = args[i + 1];
                    i++;
                    break;
            default:
                logger.error("Command" + s + " not recognized.");
                logger.info(Constants.HELP_TEXT);
                return;
            }
            i++;
        }
        logger.info("Validating: " + ontology);
        if(outPath.isEmpty()){
            outPath = Constants.DEFAULT_OUT_PATH;
        }
        FOOPS f = null;
        try {
            f = new FOOPS(ontology, isFromFile);
            f.fairTest();
            PrintWriter out = new PrintWriter(outPath);
            out.println(f.exportJSON());
            out.close();
//            f.exportNormalizedMetadataToRDF("test.ttl");
        }catch(Exception e){
            logger.error("Error! "+ e.getMessage());
        }finally{
            if (f != null){
                f.removeTemporaryFolders();
            }
        }
    }
}

