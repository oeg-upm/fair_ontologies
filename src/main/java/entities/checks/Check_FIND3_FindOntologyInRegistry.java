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
package entities.checks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.Check;
import entities.Ontology;
import fair.Constants;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This check looks if the ontology can be found in LOV.
 * We download all ontologies instead of looking at the particular API because:
 *  - It's only around 800, not big deal.
 *  - I would like to look by namespaces, not prefixes.
 */
public class Check_FIND3_FindOntologyInRegistry extends Check {
    public Check_FIND3_FindOntologyInRegistry(Ontology o) {
        super(o);
        this.id = Constants.FIND3;
        this.description = Constants.FIND3_DESC;
        this.category_id = Constants.FINDABLE;
        this.principle_id = "F4";
    }

    @Override
    public void check() {
        super.check();
        if (!this.status.equals("unchecked")){
            //this check has already been checked, return
            return;
        }
        String ontoURI = this.ontology_URI;
        // remove trailing slash/hash for making comparisons easier.
        if (ontoURI.endsWith("/") || ontoURI.endsWith("#")){
            ontoURI = ontoURI.substring(0, ontoURI.length()-1);
        }
//        if ("".equals(ontoPrefix) || ontoPrefix == null){
//            this.status = Constants.ERROR;
//            this.explanation = "No prefix declared in the ontology";
//            return;
//        }
        try {
            URL url = new URL(Constants.LOV_ALL_VOCABS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = (InputStream) connection.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer, "UTF-8");
            try {
                JsonArray vocabularies = JsonParser.parseString(writer.toString()).getAsJsonArray();
                boolean found = false;
                for (JsonElement e:vocabularies){
                    String voc_ns = ((JsonObject)e).get("uri").getAsString();
                    if (voc_ns.endsWith("/") || voc_ns.endsWith("#")){
                        voc_ns = voc_ns.substring(0, voc_ns.length()-1);
                    }
                    if (voc_ns.equals((ontoURI))){
                        found = true;
                        break;
                    }
                }
                if(found){
                    this.total_passed_tests ++;
                    this.status = Constants.OK;
                    this.explanation = Constants.FIND3_EXPLANATION_OK + " LOV repository";
                }else{
                    // TO DO: Try ontobee, bioportal. For now we just report error
                    this.status = Constants.ERROR;
                    this.explanation = Constants.FIND3_EXPLANATION_ERROR;
                }
            }catch(Exception e){
                this.status = Constants.ERROR;
                this.explanation = Constants.FIND3_EXPLANATION_ERROR;
                // TO DO: Try ontobee, bioportal. For now we just report error
            }

            in.close();
        }catch(Exception e){
            this.status = Constants.ERROR;
            this.explanation = Constants.FIND3_EXPLANATION_ERROR;
        }
    }
}
