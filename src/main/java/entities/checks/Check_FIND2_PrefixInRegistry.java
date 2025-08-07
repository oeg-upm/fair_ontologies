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


import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import entities.Check;
import entities.Ontology;
import fair.Constants;

/**
 * This check verifies whether the ontology can be found in prefix.cc or not.
 * Note: A cache could help avoid doing this request.
 */

public class Check_FIND2_PrefixInRegistry extends Check {
    public Check_FIND2_PrefixInRegistry(Ontology o) {
        super(o);
        this.id = Constants.FIND2_URL;
        this.title = Constants.FIND2_TITLE;
        this.description = Constants.FIND2_DESC;
        this.category_id = Constants.FINDABLE;
        this.principle_id = "F4";
        this.total_tests_run = 2;
        //one test to see if the onto is there, another one to check if the namespace is the right one
        this.abbreviation = Constants.FIND2;
    }

    @Override
    public void check() {
        super.check();
        String ontoPrefix = this.ontology.getNamespacePrefix();
        String ontoURI = this.ontology_URI;
        //remove trailing slash/hash for making comparisons easier.
        if (ontoURI.endsWith("/") || ontoURI.endsWith("#")){
            ontoURI = ontoURI.substring(0, ontoURI.length()-1);
        }
        if ("".equals(ontoPrefix) || ontoPrefix == null){
            this.status = Constants.ERROR;
            this.explanation = "No prefix declared in the ontology";
            return;
        }
        // Prefix.cc
        getPrefix(Constants.PREFIX_CC+ontoPrefix+".file.json", ontoPrefix, ontoURI);
        int total_passed_test_aux = 0;
        String explanation_aux = "";
        // LOV
        if(this.status.equals(Constants.ERROR)){
            total_passed_test_aux = total_passed_tests;
            explanation_aux = explanation;
            total_passed_tests = 0;
            explanation = "";
            getPrefix(Constants.LOV_PREFIX_VOCAB + ontoPrefix, "nsp", ontoURI);
        }
        if (this.status.equals(Constants.ERROR) && total_passed_test_aux > 0){
            total_passed_tests = total_passed_test_aux;
            explanation = explanation_aux;
        }
    }

    private void getPrefix(String urlAPI, String fieldToRetrieveNs, String ontoURI){
        try {
            URL url = new URL(urlAPI);
            String platform;
            if (urlAPI.contains("prefix.cc")){
                platform = "prefix.cc";
            }else{
                platform = "LOV";
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer, "UTF-8");
            try {
                JsonObject jsonObjectAlt = JsonParser.parseString(writer.toString()).getAsJsonObject();
                String ns = jsonObjectAlt.getAsJsonObject().get(fieldToRetrieveNs).getAsString();
                if (ns != null && !ns.isEmpty()){
                    if (ns.endsWith("/") || ns.endsWith("#")){
                        ns = ns.substring(0, ns.length()-1);
                    }
                    this.total_passed_tests ++;
                    if (ns.equals(ontoURI)){
                        this.total_passed_tests++;
                        this.status = Constants.OK;
                        this.explanation = Constants.FIND2_EXPLANATION_OK + " (in "+platform+")";
                    }else{
                        this.status = Constants.ERROR;
                        this.explanation = Constants.FIND2_EXPLANATION_OK_ALMOST+". Prefix found in "+platform+": "+ns;
                    }
                }else {
                    this.status = Constants.ERROR;
                    this.explanation = Constants.FIND2_EXPLANATION_ERROR;
                }
            }catch(Exception e){
                this.status = Constants.ERROR;
                this.explanation = Constants.FIND2_EXPLANATION_ERROR;//prefix not found!
            }

            in.close();
        }catch(Exception e){
            this.status = Constants.ERROR;
            this.explanation = "Error when retrieving prefix";
        }
    }
}
