/*
 * Copyright 2012-2013 Ontology Engineering Group, Universidad Politecnica de Madrid, Spain
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
 */

package fair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Check;
import entities.checks.CheckContentNegotiation;
import entities.checks.CheckPersistentURIs;
import entities.Ontology;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

//import serialization.InterfaceAdapter;

public class FOOPS {
    private static final Logger logger = LoggerFactory.getLogger(FOOPS.class);

    private boolean isFromFile;
    private ArrayList<Check> checks;
    private Path tmpFolder;
    private Ontology ontology;

    public FOOPS(String o, boolean isFromFile){
        this.isFromFile = isFromFile;
        tmpFolder = null;
        try {
            tmpFolder = Files.createTempDirectory(Path.of("."), "foops");
        }catch(Exception e){
            logger.error("Could not create temporary folder. Exiting");
            return;
        }
        this.ontology = new Ontology(o, isFromFile, tmpFolder);
        //TO DO: initialize all checks here.
        CheckPersistentURIs f1 = new CheckPersistentURIs(ontology);
        CheckContentNegotiation a1 = new CheckContentNegotiation(ontology);
        checks = new ArrayList<>();
        checks.add(a1);
        checks.add(f1);
    }

    /**
     * Method for passing all the checks.
     */
    private void fairTest(){
        checks.forEach(check -> check.check());
    }

    private float getTotalScore(){
        float totalNum = 0, totalDenom = 0;
        for (Check check : checks) {
            totalNum += check.getTotal_passed_tests();
            totalDenom += check.getTotal_tests_run();
        }
        return totalNum/totalDenom;
    }

    /**
     * This method writes the results as a JSON file
     */
    private void exportJSON(String path){
        String out = "{\n\"ontology_URI\": \""+this.ontology.getOntologyURI()+"\",\n" +
                "\"ontology_title\": \""+this.ontology.getTitle()+"\",\n" +
                "\"overall_score\":"+this.getTotalScore()+",\n" +
                "\"checks\":";
        Gson gson = new GsonBuilder().
                excludeFieldsWithoutExposeAnnotation().
                setPrettyPrinting().
                create();
        String jsonChecks = gson.toJson(checks);
        out += jsonChecks +"\n}";
        System.out.println(out);
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
            f.exportJSON(outPath);

        }catch(Exception e){
            logger.error("Error!");
            e.printStackTrace();
        }finally{
            if (f != null){
                f.removeTemporaryFolders();
            }
        }
    }
}

