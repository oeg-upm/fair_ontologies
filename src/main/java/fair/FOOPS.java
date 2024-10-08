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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Check;
import entities.checks.*;
import entities.Ontology;
import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class FOOPS {
    private static final Logger logger = LoggerFactory.getLogger(FOOPS.class);

    private ArrayList<Check> checks;
    private Path tmpFolder;
    private Ontology ontology;

    public FOOPS(String o, boolean isFromFile){
        tmpFolder = null;
        try {
            tmpFolder = Files.createTempDirectory(Path.of("."), "foops");
        }catch(Exception e){
            logger.error("Could not create temporary folder. Exiting");
            return;
        }
        this.ontology = new Ontology(o, isFromFile, tmpFolder);
        Check_PURL1_PersistentURIs f1 = new Check_PURL1_PersistentURIs(ontology);
        Check_URI1_URIResolvable uri1 = new Check_URI1_URIResolvable(ontology);
        Check_CN1_ContentNegotiation a1 = new Check_CN1_ContentNegotiation(ontology);
        Check_DOC1_HTMLDoc d1 = new Check_DOC1_HTMLDoc(ontology);
        Check_RDF1_RDFAvailability rdf1 = new Check_RDF1_RDFAvailability(ontology);
        Check_OM1_MinimumMetadata om1 = new Check_OM1_MinimumMetadata(ontology);
        Check_OM2_RecommendedMetadata om2 = new Check_OM2_RecommendedMetadata(ontology);
        Check_OM3_DetailedMetadata om3 = new Check_OM3_DetailedMetadata(ontology);
        Check_OM4_1_License om41 = new Check_OM4_1_License(ontology);
        Check_OM4_2_LicenseIsResolvable om42 = new Check_OM4_2_LicenseIsResolvable(ontology);
        Check_OM5_1_ProvenanceMetadataBasic om51 = new Check_OM5_1_ProvenanceMetadataBasic(ontology);
        Check_OM5_2_ProvenanceMetadataFull om52 = new Check_OM5_2_ProvenanceMetadataFull(ontology);
        Check_FIND1_Prefix find1 = new Check_FIND1_Prefix(ontology);
        Check_FIND2_PrefixInRegistry find2 = new Check_FIND2_PrefixInRegistry(ontology);
        Check_FIND3_FindOntologyInRegistry find3 = new Check_FIND3_FindOntologyInRegistry(ontology);
        Check_FIND3_BIS_MetadataAccessible find3_bis = new Check_FIND3_BIS_MetadataAccessible(ontology,find3);
        Check_HTTP1_AccessProtocol http1 = new Check_HTTP1_AccessProtocol(ontology);
        Check_VOC1_VocabReuseMetadata voc1 = new Check_VOC1_VocabReuseMetadata(ontology);
        Check_VOC2_VocabReuse voc2 = new Check_VOC2_VocabReuse(ontology);
        Check_VOC3_TermMetadataLabel voc3 = new Check_VOC3_TermMetadataLabel(ontology);
        Check_VOC4_TermMetadataDescription voc4 = new Check_VOC4_TermMetadataDescription(ontology);
        Check_VER1_VersionIRI ver1 = new Check_VER1_VersionIRI(ontology);
        Check_VER2_ResolvableVersionIRI ver2 = new Check_VER2_ResolvableVersionIRI(ontology);
        checks = new ArrayList<>();
        checks.add(a1);
        checks.add(f1);
        checks.add(uri1);
        checks.add(d1);
        checks.add(rdf1);
        checks.add(om1); checks.add(om2); checks.add(om3);
        checks.add(om41); checks.add(om42);
        checks.add(om51); checks.add(om52);
        checks.add(find1); checks.add(find2); checks.add(find3); checks.add(find3_bis);
        checks.add(http1);
        checks.add(voc1); checks.add(voc2); checks.add(voc3); checks.add(voc4);
        checks.add(ver1); checks.add(ver2);
        //only add this check if ontology was loaded through it URI
        if(!isFromFile){
            Check_URI2_OntologyURIEqualToID uri2 = new Check_URI2_OntologyURIEqualToID(ontology, o);
            checks.add(uri2);
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

