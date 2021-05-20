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
 */

package entities;

import com.google.gson.annotations.Expose;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public abstract class Check {
    /**
     * Note: some of these do not follow camelcase to show nice JSON
     */
    @Expose (serialize = true)
    protected String id;
    @Expose (serialize = true)
    protected String principle_id;
    @Expose (serialize = true)
    protected String category_id;
    @Expose (serialize = true)
    protected String status = "unchecked";
    @Expose (serialize = true)
    protected String explanation;
    @Expose (serialize = true)
    protected ArrayList<String> affected_Elements;
    @Expose (serialize = true)
    protected String description;
    protected OWLOntology ontology;
    protected String ontology_URI;
    @Expose (serialize = true)
    protected int total_passed_tests;
    @Expose (serialize = true)
    protected int total_tests_run; // in case a check does more than one assessment

    public Check(Ontology o){
        this.ontology = o.getOntologyModel();
        this.ontology_URI = o.getOntologyURI();
        total_passed_tests = 0;
        total_tests_run = 1; //by default
    }

    public String getDescription() {
        return description;
    }

    public String getOntology_URI() {
        return ontology_URI;
    }

    public String getPrinciple_id() {
        return principle_id;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public String getCategory_id() {
        return category_id;
    }

    public ArrayList<String> getAffected_Elements() {
        return affected_Elements;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAffected_Elements(ArrayList<String> affected_Elements) {
        this.affected_Elements = affected_Elements;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal_tests_run() {
        return total_tests_run;
    }

    public int getTotal_passed_tests() {
        return total_passed_tests;
    }

    /**
     * Method to be extended by the different checks to perform the appropriate validation or test
     */
    public void check(){
        LoggerFactory.getLogger(Check.class).info("Checking "+this.id);
    }

}
