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

package entities;

import com.google.gson.annotations.Expose;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import fair.Constants;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    protected String title;
    @Expose (serialize = true)
    protected String explanation;

    @Expose (serialize = true)
    protected String abbreviation;
    @Expose (serialize = true)
    protected String action;
    @Expose (serialize = true)
    protected String recommendedDoc;
    @Expose (serialize = true)
    protected String[] affectedElements;
    @Expose (serialize = true)
    protected ArrayList<String> affected_elements;
    @Expose (serialize = true)
    protected String description;
    protected Ontology ontology;
    protected String ontology_URI;
    @Expose (serialize = true)
    protected int total_passed_tests;
    @Expose (serialize = true)
    protected int total_tests_run; // in case a check does more than one assessment
    @Expose (serialize = true)
    protected ArrayList<String> reference_resources; //any other URI we may want to return.
    @Expose (serialize = true)
    protected String guidance;

    public Check(Ontology o){
        this.ontology = o;
        if(this.ontology!=null) {
            this.ontology_URI = o.getOntologyURI();
        }
        total_passed_tests = 0;
        total_tests_run = 1; //by default
    }


    public String getAbbreviation(){ return abbreviation;}

    public void setAbbreviation(String abbreviation){ this.abbreviation = abbreviation; }
    public String getDescription() {
        return description;
    }

    public String getOntology_URI() {
        return ontology_URI;
    }

    public String getPrinciple_id() {
        return principle_id;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public String getCategory_id() {
        return category_id;
    }

    public ArrayList<String> getAffected_Elements() {
        return affected_elements;
    }

    public String getExplanation() {
        return explanation;
    }

    public ArrayList<String> getReference_resources(){return  reference_resources;}

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
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

    public void setAffected_elements(ArrayList<String> affected_Elements) {
        this.affected_elements = affected_elements;
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

    public String getAction(){
        return action;
    }

    public void setAction(String action){
        this.action = action;
    }
    public String getRecommendedDoc(){
        return recommendedDoc;
    }
    public void setRecommendedDoc(String recommendedDoc){
        this.recommendedDoc = recommendedDoc;
    }
    public String getAffectedElements(){
        return affectedElements.toString();
    }
    public String getGuidance(){
        return guidance;
    }
    public void setGuidance(String guidance){
        this.guidance = guidance;
    }
    /**
     * Method to be extended by the different checks to perform the appropriate validation or test
     */
    public void check(){
        LoggerFactory.getLogger(Check.class).info("Checking "+this.id);
    }

    public String fillAction(String template) {
        if (template == null) return null;
        return template
            .replace("$ONTOLOGY_URI", ontology_URI != null ? ontology_URI : "your ontology")
            .replace("$NAMESPACE_PREFIX", 
                ontology.getNamespacePrefix() != null ? ontology.getNamespacePrefix() : "your prefix")
            .replace("$VERSION_IRI", 
                ontology.getVersionIRI() != null && !ontology.getVersionIRI().isEmpty() 
                    ? ontology.getVersionIRI() 
                    : (ontology_URI != null ? ontology_URI : "your version IRI"))
            .replace("$ONTOLOGY_TITLE", 
                ontology.getTitle() != null ? ontology.getTitle() : "your ontology")
            .replace("$LICENSE_URI", 
                ontology.getLicense() != null ? ontology.getLicense() : "https://creativecommons.org/licenses/by/4.0/");
    }

    public String buildMetadataSnippet(String[] metadataKeys, String[] optionalKeys) {
        Set<String> optional = optionalKeys != null ? new HashSet<>(Arrays.asList(optionalKeys)) : new HashSet<>();

        String[] allKeys;
        if (optionalKeys != null && optionalKeys.length > 0) {
            allKeys = Arrays.copyOf(metadataKeys, metadataKeys.length + optionalKeys.length);
            System.arraycopy(optionalKeys, 0, allKeys, metadataKeys.length, optionalKeys.length);
        } else {
            allKeys = metadataKeys;
        }
        String ontoURI = this.ontology_URI != null ? this.ontology_URI : "https://w3id.org/example#";
        StringBuilder sb = new StringBuilder();
        sb.append("<pre><code>");
        sb.append("&lt;").append(ontoURI).append("&gt; a owl:Ontology ;\n");

        for (int i = 0; i < allKeys.length; i++) {
            String key = allKeys[i];
            boolean found = this.ontology != null && 
                this.ontology.getSupportedMetadata().contains(key);
            String line = getTurtleLine(key, ontoURI, found);
            String suffix = (i == allKeys.length - 1) ? " ." : " ;";
            if (found) {
                sb.append("    ").append(line).append(suffix).append("\n");
            // } else if (optional.contains(key)) {
            //     sb.append("    <span style=\"color:darkorange\">").append(line).append(suffix)
            //   .append("</span>\n");
            } else {
                String annotation = optional.contains(key) ? "    ## OPTIONAL" : "";
                sb.append("    <span class=\"missing-property\">").append(line).append(suffix)
                .append(annotation).append("</span>\n");
            }
        }

        sb.append("</code></pre>");
        return sb.toString();
    }

    private String getTurtleLine(String key, String ontoURI, boolean found) {
        switch (key) {
            case Constants.FOOPS_TITLE:
                String t = found ? ontology.getTitle() : "Your ontology title";
                return "dc:title \"" + (t != null ? t : "Your ontology title") + "\"@en";
            case Constants.FOOPS_DESCRIPTION:
                String d = found ? ontology.getDescription() : "Brief description of your ontology.";
                return "dc:description \"" + (d != null ? d : "Brief description of your ontology.") + "\"@en";
            case Constants.FOOPS_LICENSE:
                String l = found ? ontology.getLicense() : "https://creativecommons.org/licenses/by/4.0/";
                return "dc:license &lt;" + (l != null ? l : "https://creativecommons.org/licenses/by/4.0/") + "&gt;";
            case Constants.FOOPS_VERSION_IRI:
                String v = found ? ontology.getVersionIRI() : ontoURI + "/1.0.0";
                return "owl:versionIRI &lt;" + (v != null ? v : ontoURI + "/1.0.0") + "&gt;";
            case Constants.FOOPS_AUTHOR:
                if (found && ontology.getAuthors() != null && !ontology.getAuthors().isEmpty()) {
                    String authors = String.join(", ", ontology.getAuthors());
                    return "dc:creator \"" + authors + "\"";
                }
                return "dc:creator &lt;https://w3id.org/people#AuthorURI&gt;";
            case Constants.FOOPS_NS_URI:
                if (found && ontology.getNamespaceUri() != null) {
                    return "vann:preferredNamespaceUri &lt;" + ontology.getNamespaceUri() + "&gt;";
                }
                return "vann:preferredNamespaceUri &lt;" + ontoURI + "#&gt;";
            case Constants.FOOPS_NS_PREFIX:
                if (found && ontology.getNamespacePrefix() != null) {
                    return "vann:preferredNamespacePrefix \"" + ontology.getNamespacePrefix() + "\"";
                }
                return "vann:preferredNamespacePrefix \"your-prefix\"";
            case Constants.FOOPS_VERSION_INFO:
                if (found && ontology.getVersionInfo() != null) {
                    return "owl:versionInfo \"" + ontology.getVersionInfo() + "\"";
                }
                return "owl:versionInfo \"1.0.0\"";
            case Constants.FOOPS_CREATION_DATE:
                String c = found ? ontology.getCreationDate() : "2021-01-01";
                return "dcterms:created \"" + (c != null ? c : "2021-01-01") + "\"^^xsd:date";
            case Constants.FOOPS_CITATION:
                if (found && ontology.getCitation() != null) {
                    return "dcterms:bibliographicCitation \"" + ontology.getCitation() + "\"";
                }
                return "dcterms:bibliographicCitation \"Your citation text\"";
            case Constants.FOOPS_CONTRIBUTOR:
                if (found && ontology.getContributors() != null && !ontology.getContributors().isEmpty()) {
                    String contributors = String.join(", ", ontology.getContributors());
                    return "dcterms:contributor \"" + contributors + "\"";
                }
                return "dcterms:contributor &lt;https://w3id.org/people#AContributorURI&gt;";
            case Constants.FOOPS_DOI:
                String doi = found ? ontology.getDoi() : "https://doi.org/10.xxxx/xxxxx";
                return "bibo:doi &lt;" + (doi != null ? doi : "https://doi.org/10.xxxx/xxxxx") + "&gt;";
            case Constants.FOOPS_PUBLISHER:
                String pub = found ? ontology.getPublisher() : "https://w3id.org/people#Publisher";
                return "dcterms:publisher &lt;" + (pub != null ? pub : "https://w3id.org/people#Publisher") + "&gt;";
            case Constants.FOOPS_LOGO:
                String logo = found ? ontology.getLogo() : "https://example.org/logo.png";
                return "schema:logo &lt;" + (logo != null ? logo : "https://example.org/logo.png") + "&gt;";
            case Constants.FOOPS_STATUS:
                String st = found ? ontology.getStatus() : "bibo:status/Published";
                return "bibo:status &lt;" + (st != null ? st : "http://purl.org/ontology/bibo/status/Published") + "&gt;";
            case Constants.FOOPS_SOURCE:
                String src = found ? ontology.getSource() : "https://example.org/source";
                return "dcterms:source &lt;" + (src != null ? src : "https://example.org/source") + "&gt;";
            case Constants.FOOPS_ISSUED:
                String iss = found ? ontology.getIssuedDate() : "2021-01-01";
                return "dcterms:issued \"" + (iss != null ? iss : "2021-01-01") + "\"^^xsd:date";
            case Constants.FOOPS_PREVIOUS_VERSION:
                String pv = found ? ontology.getPreviousVersion() : ontoURI + "/0.9.0";
                return "owl:priorVersion &lt;" + (pv != null ? pv : ontoURI + "/0.9.0") + "&gt;";
            case Constants.FOOPS_B_COMPATIBILITY:
                return "owl:backwardCompatibleWith &lt;" + ontoURI + "/0.9.0&gt;";
            case Constants.FOOPS_MODIFIED:
                String mod = found ? ontology.getModifiedDate() : "2021-06-01";
                return "dcterms:modified \"" + (mod != null ? mod : "2021-06-01") + "\"^^xsd:date";
                        default:
                return "";
        }
    }
}
