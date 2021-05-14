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

import entities.A1;
import entities.Check;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FOOPS {
    private static final Logger logger = LoggerFactory.getLogger(FOOPS.class);

    private boolean isFromFile;
    private OWLOntology o;
    private ArrayList<Check> checks;

    public FOOPS(OWLOntology o, boolean isFromFile){
        this.o = o;
        this.isFromFile = isFromFile;

        //TO DO: initialize all checks here.
        A1 a1 = new A1();
        checks = new ArrayList<>();
        checks.add(a1);
    }

    /**
     * Method for passing all the checks.
     */
    private void fairTest(){
        checks.forEach(check -> check.check());
    }

    /**
     * This method writes the results as a JSON file
     */
    private void exportJSON(String path){
        // TO DO
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
        try {
            OWLOntology onto = Utils.loadModelToDocument(ontology, isFromFile);
            FOOPS f = new FOOPS(onto, isFromFile);
            f.fairTest();
            f.exportJSON(outPath);

        }catch(Exception e){
            logger.error("Could not load your ontology. Are you sure it is accessible?");
            e.printStackTrace();
        }
    }
}

