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
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * This check looks if the ontology can be found in LOV.
 * We download all ontologies instead of looking at the particular API because:
 *  - It's only around 800, not big deal.
 *  - I would like to look by namespaces, not prefixes.
 */
public class Check_FIND3_FindOntologyInRegistry extends Check {
    public Check_FIND3_FindOntologyInRegistry(Ontology o) {
        super(o);
        this.id = Constants.FIND3_URL;
        this.title = Constants.FIND3_TITLE;
        this.description = Constants.FIND3_DESC;
        this.category_id = Constants.FINDABLE;
        this.principle_id = "F4";
        this.abbreviation = Constants.FIND3;
    }


    @Override
    public void check() {
        super.check();
        if (!this.status.equals("unchecked")){
            //this check has already been checked, return
            return;
        }
        if(this.getOntology().getSupportedMetadata().contains(Constants.FOOPS_INCLUDED_IN_DATA_CATALOG)){
            this.total_passed_tests ++;
            this.status = Constants.OK;
            this.explanation = Constants.FIND3_EXPLANATION_OK_ANN;
            return;
        }
        String ontoURI = this.ontology_URI;
        String namespaceURI = this.ontology.getNamespaceUri();
        // remove trailing slash/hash for making comparisons easier.
        if (ontoURI.endsWith("/") || ontoURI.endsWith("#")){
            ontoURI = ontoURI.substring(0, ontoURI.length()-1);
        }
        //in case the ns URI is different from the URI of the ontology
        if (namespaceURI.endsWith("/") || namespaceURI.endsWith("#")){
            namespaceURI = namespaceURI.substring(0, namespaceURI.length()-1);
        }
        try {
            URL url = new URL(Constants.LOV_ALL_VOCABS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.LOV_CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(Constants.LOV_READ_TIMEOUT_MS);
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
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
                    if (voc_ns.equals((ontoURI)) || voc_ns.equals((namespaceURI))){
                        found = true;
                        break;
                    }
                }
                if(found){
                    this.total_passed_tests ++;
                    this.status = Constants.OK;
                    this.explanation = Constants.FIND3_EXPLANATION_OK + " LOV repository";
                }else{
                    boolean foundInOntobee = checkOntobee(ontoURI) || checkOntobee(namespaceURI);
                    if (foundInOntobee) {
                        this.total_passed_tests++;
                        this.status = Constants.OK;
                        this.explanation = Constants.FIND3_EXPLANATION_OK + " ontobee";
                    } else {
                        this.status = Constants.ERROR;
                        this.explanation = Constants.FIND3_EXPLANATION_ERROR;
                    }
                }
            }catch(Exception e){
                if (checkOntobee(ontoURI) || checkOntobee(namespaceURI)) {
                    this.total_passed_tests++;
                    this.status = Constants.OK;
                    this.explanation = Constants.FIND3_EXPLANATION_OK + " ontobee";
                    return;
                }
                this.status = Constants.ERROR;
                this.explanation = Constants.FIND3_EXPLANATION_ERROR;
            }

            in.close();
        }catch(Exception e){
            if (checkOntobee(ontoURI) || checkOntobee(namespaceURI)) {
                this.total_passed_tests++;
                this.status = Constants.OK;
                this.explanation = Constants.FIND3_EXPLANATION_OK + " ontobee";
            } else {
                this.status = Constants.ERROR;
                this.explanation = Constants.FIND3_EXPLANATION_ERROR;
            }
        }
    }

    private boolean checkOntobee(String uri) {
        try {
            String query = "ASK WHERE { GRAPH ?g { <" + uri + "> ?p ?o } }";
            URL url = new URL(Constants.ONTOBEE_SPARQL_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(Constants.ONTOBEE_CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(Constants.ONTOBEE_READ_TIMEOUT_MS);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/sparql-results+json");
            conn.setDoOutput(true);
            String body = "query=" + URLEncoder.encode(query, "UTF-8");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes("UTF-8"));
            }
            try (InputStream in = conn.getInputStream()) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer, "UTF-8");
                JsonObject result = JsonParser.parseString(writer.toString()).getAsJsonObject();
                return result.get("boolean").getAsBoolean();
            }
        } catch (Exception e) {
            return false; 
        }
    }
}
