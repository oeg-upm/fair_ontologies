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

import fair.Constants;
import fair.Utils;
import org.jsoup.nodes.Document;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ontology {
    private OWLOntology ontologyModel;
    private Document htmlDocumentation;
    private static final Logger logger = LoggerFactory.getLogger(Ontology.class);

    //metadata attributes
    private String ontologyURI;
    private String namespacePrefix;
    private String title;
    private String description;
    private String versionIRI;
    private String versionInfo;
    private ArrayList<String> authors;
    private ArrayList<String> contributors;
    private String license;
    private String status;
    private String previousVersion;
    private String creationDate;
    private String issuedDate;
    private String modifiedDate;
    private String source;
    private String backwardCompatibility;
    private String publisher;
    private String citation;
    private String doi;
    private String logo;
    private ArrayList<String> supportedMetadata;
    private ArrayList<String> reusedMetadataVocabularies;
    private ArrayList<String> reusedVocabularies;
    List<OWLImportsDeclaration> importedVocabularies;
    private ArrayList<String> termsWithLabel;
    private ArrayList<String> terms; // all terms
    private ArrayList<String> termsWithDescription;

    /**
     *
     * @param o ontology path or URI
     * @param isFromFile flag to indicate whether the ontology should be loaded from file or URL
     * @param tmpFolder temporary folder where to store the downloaded ontology
     */
    public Ontology(String o, boolean isFromFile, Path tmpFolder){
        supportedMetadata = new ArrayList<>();
        reusedMetadataVocabularies = new ArrayList<>();
        reusedVocabularies = new ArrayList<>();
        termsWithLabel = new ArrayList<>();
        termsWithDescription = new ArrayList<>();
        terms = new ArrayList<>();
        //Download ontology (any serialization)
        try {
            this.ontologyModel = Utils.loadModelToDocument(o, isFromFile, tmpFolder.toString());
            this.ontologyURI = this.ontologyModel.getOntologyID().getOntologyIRI().get().toString();
        }catch(Exception e){
            logger.error("Could not load the ontology");
            if(!isFromFile){
                ontologyURI = o.strip();
            }
        }
        //Download HTML of the onto (if available)
        try{
            htmlDocumentation = Utils.loadOntologyHTML(ontologyURI);
            //, tmpFolder.toString() + File.separator+ "ontology.html");
        }
        catch(Exception e){
            logger.error("Could not load ontology in HTML");
            htmlDocumentation = null;
        }
        try {
            this.getOntologyMetadata();
            this.getOntologyCoverage();
        }catch(Exception e){
            logger.error("Error when extracting metadata or annotations");
        }
    }

    /**
     * Method that loads all available ontology metadata, using a series of common vocabularies as reference
     */
    private void getOntologyMetadata(){
        logger.info("Retrieving ontology metadata");
        if (this.ontologyModel == null) {
            logger.error("No ontology loaded! Metadata cannot be extracted");
            return;
        }
        try {
            this.versionIRI = this.ontologyModel.getOntologyID().getVersionIRI().get().toString();
            this.supportedMetadata.add(Constants.FOOPS_VERSION_IRI);
        } catch (Exception e) {
            logger.info("No version IRI detected");
        }
        this.authors = new ArrayList<>();
        this.contributors = new ArrayList<>();
        this.title = "Title unavailable";
        this.ontologyModel.annotations().forEach(a -> completeMetadata(a));
        /*if (isUseLicensius()) {
            String licName;
            String lic = GetLicense.getFirstLicenseFound(mainOntologyMetadata.getNamespaceURI());
            if (!lic.isEmpty() && !lic.equals("unknown")) {
                mainOntologyMetadata.getLicense().setUrl(lic);
                licName = GetLicense.getTitle(lic);
                mainOntologyMetadata.getLicense().setName(licName);
            }
        }*/
    }

    private void completeMetadata(OWLAnnotation a) {
        String propertyName = a.getProperty().getIRI().getIRIString();
        String value;
        switch (propertyName) {
//            case Constants.PROP_RDFS_LABEL:
//                try {
//                    valueLanguage = a.getValue().asLiteral().get().getLang();
//                    value = a.getValue().asLiteral().get().getLiteral();
//                } catch (Exception e) {
//                    logger.error("Error while getting ontology label. No literal provided");
//                }
//                break;
            case Constants.PROP_DC_TITLE:
            case Constants.PROP_DCTERMS_TITLE:
            case Constants.PROP_SCHEMA_NAME:
                try {
//                    valueLanguage = a.getValue().asLiteral().get().getLang();
                    this.title = a.getValue().asLiteral().get().getLiteral();
                    this.supportedMetadata.add(Constants.FOOPS_TITLE);
                } catch (Exception e) {
                    logger.error("Error while getting ontology title. No literal provided");
                }
                break;
            case Constants.PROP_DCTERMS_ABSTRACT:
            case Constants.PROP_DC_ABSTRACT:
            case Constants.PROP_DCTERMS_DESCRIPTION:
            case Constants.PROP_DC_DESCRIPTION:
            case Constants.PROP_SCHEMA_DESCRIPTION:
            case Constants.PROP_RDFS_COMMENT:
            case Constants.PROP_SKOS_NOTE:
                try {
//                    valueLanguage = a.getValue().asLiteral().get().getLang();
                    this.description = a.getValue().asLiteral().get().getLiteral();
                    this.supportedMetadata.add(Constants.FOOPS_DESCRIPTION);
                } catch (Exception e) {
                    logger.error("Error while getting ontology abstract. No literal provided");
                }
                break;
            case Constants.PROP_DCTERMS_REPLACES:
            case Constants.PROP_DC_REPLACES:
            case Constants.PROP_PROV_WAS_REVISION_OF:
            case Constants.PROP_OWL_PRIOR_VERSION:
            case Constants.PROP_PAV_PREVIOUS_VERSION:
                this.previousVersion = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_PREVIOUS_VERSION);
                break;
            case Constants.PROP_OWL_VERSION_INFO:
            case Constants.PROP_SCHEMA_SCHEMA_VERSION:
                try {
                    this.versionInfo = a.getValue().asLiteral().get().getLiteral();
                    this.supportedMetadata.add(Constants.FOOPS_VERSION_INFO);
                } catch (Exception e) {
                    logger.error("Error while getting ontology abstract. No literal provided");
                }
                break;
            case Constants.PROP_VANN_PREFIX:
                this.namespacePrefix = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_NS_PREFIX);
                break;
            case Constants.PROP_VANN_URI:
                value = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_NS_URI);
                if (!this.ontologyURI.equals(value)) {
                    logger.warn("Ontology NS URI declared and annotated is not the same!");
                }
                break;
            case Constants.PROP_DCTERMS_LICENSE:
            case Constants.PROP_DC_RIGHTS:
            case Constants.PROP_SCHEMA_LICENSE:
            case Constants.PROP_CC_LICENSE:
                try {
                    this.license = Utils.getValueAsLiteralOrURI(a.getValue());
                    this.supportedMetadata.add(Constants.FOOPS_LICENSE);
                } catch (Exception e) {
                    logger.error("Could not retrieve license. Please avoid using blank nodes...");
                }
                break;
            case Constants.PROP_DC_CONTRIBUTOR:
            case Constants.PROP_DCTERMS_CONTRIBUTOR:
            case Constants.PROP_SCHEMA_CONTRIBUTOR:
            case Constants.PROP_PAV_CONTRIBUTED_BY:
            case Constants.PROP_DC_CREATOR:
            case Constants.PROP_DCTERMS_CREATOR:
            case Constants.PROP_SCHEMA_CREATOR:
            case Constants.PROP_PAV_CREATED_BY:
            case Constants.PROP_PROV_ATTRIBUTED_TO:
            case Constants.PROP_DC_PUBLISHER:
            case Constants.PROP_DCTERMS_PUBLISHER:
            case Constants.PROP_SCHEMA_PUBLISHER:
                try {
                    value = Utils.getValueAsLiteralOrURI(a.getValue());
                    switch (propertyName) {
                        case Constants.PROP_DC_CONTRIBUTOR:
                        case Constants.PROP_DCTERMS_CONTRIBUTOR:
                        case Constants.PROP_SCHEMA_CONTRIBUTOR:
                        case Constants.PROP_PAV_CONTRIBUTED_BY:
                            this.contributors.add(value);
                            this.supportedMetadata.add(Constants.FOOPS_CONTRIBUTOR);
                            break;
                        case Constants.PROP_DC_CREATOR:
                        case Constants.PROP_DCTERMS_CREATOR:
                        case Constants.PROP_PAV_CREATED_BY:
                        case Constants.PROP_PROV_ATTRIBUTED_TO:
                        case Constants.PROP_SCHEMA_CREATOR:
                            this.authors.add(value);
                            this.supportedMetadata.add(Constants.FOOPS_AUTHOR);
                            break;
                        default:
                            this.publisher = value;
                            this.supportedMetadata.add(Constants.FOOPS_PUBLISHER);
                            break;
                    }
                } catch (Exception e) {
                    logger.error("Could not retrieve creator/contributor. Please avoid using blank nodes...");
                }
                break;
            case Constants.PROP_DCTERMS_CREATED:
            case Constants.PROP_SCHEMA_DATE_CREATED:
            case Constants.PROP_PROV_GENERATED_AT_TIME:
            case Constants.PROP_PAV_CREATED_ON:
                try {
                    this.creationDate = a.getValue().asLiteral().get().getLiteral();
                    this.supportedMetadata.add(Constants.FOOPS_CREATION_DATE);
                } catch (Exception e) {
                    logger.error("Error while getting the date. No literal provided");
                }
                break;
            case Constants.PROP_DCTERMS_MODIFIED:
            case Constants.PROP_SCHEMA_DATE_MODIFIED:
                try {
                    this.modifiedDate = a.getValue().asLiteral().get().getLiteral();
                    this.supportedMetadata.add(Constants.FOOPS_MODIFIED);
                } catch (Exception e) {
                    logger.error("Error while getting the date. No literal provided");
                }
                break;
            case Constants.PROP_DCTERMS_BIBLIOGRAPHIC_CIT:
            case Constants.PROP_SCHEMA_CITATION:
                this.citation = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_CITATION);
                break;
            case Constants.PROP_BIBO_DOI:
                this.doi = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_DOI);
                break;
            case Constants.PROP_BIBO_STATUS:
                try {
                    this.status = a.getValue().asLiteral().get().getLiteral();
                    this.supportedMetadata.add(Constants.FOOPS_STATUS);
                } catch (Exception e) {
                    logger.error("Error while getting the status. No literal provided");
                }
                break;
            case Constants.PROP_OWL_BACKWARDS_COMPATIBLE:
                this.backwardCompatibility = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_B_COMPATIBILITY);
                break;
            case Constants.PROP_FOAF_LOGO:
            case Constants.PROP_SCHEMA_SCHEMA_LOGO:
                this.logo = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_LOGO);
                break;
            case Constants.PROP_DC_SOURCE:
            case Constants.PROP_DCTERMS_SOURCE:
                this.source = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_SOURCE);
                break;
            case Constants.PROP_DCTERMS_ISSUED:
                this.issuedDate = Utils.getValueAsLiteralOrURI(a.getValue());
                this.supportedMetadata.add(Constants.FOOPS_ISSUED);
                break;
        }
    }

    /**
     * Method to extract the coverage of the ontology:
     * - Reused vocabularies
     * - Imported vocabularies
     * - Terms with no label
     * - Terms with no description
     * Since this iterates over all terms of the ontology, we do it once in this method
     */
    private void getOntologyCoverage(){
        //metadata vocabularies
        this.ontologyModel.annotations().forEach(a -> checkNamespaces(a));
        //imports
        this.importedVocabularies = this.ontologyModel.importsDeclarations().collect(Collectors.toList());
        //vocabulary reuse
        logger.info("Extracting namespaces, labels, descriptions");
        this.ontologyModel.classesInSignature().forEach(a -> checkTermCoverage(a));
        this.ontologyModel.objectPropertiesInSignature().forEach(a-> checkTermCoverage(a));
        this.ontologyModel.dataPropertiesInSignature().forEach(a-> checkTermCoverage(a));
    }

    /**
     * This method checks if the metadata vocabularies used in the annotations of the ontologies match our white list
     * @param a annotation to analize
     */
    private void checkNamespaces(OWLAnnotation a){
        for (String vocab: Constants.VOCS_REUSE_METADATA){
            if (a.getProperty().getIRI().getIRIString().contains(vocab)){
                if(!reusedMetadataVocabularies.contains(vocab)) {
                    reusedMetadataVocabularies.add(vocab);
                }
            }
        }
    }

    /**
     * This method checks whether the ontology is reusing a vocabulary or not
     * @param a named object to analyze.
     */
    private void checkTermCoverage(OWLNamedObject a){
        String termNS = a.getIRI().getNamespace();
        if(termNS.equals(Constants.NS_OWL)) return; //We ignore OWL reuse.
        if(!termNS.contains(this.ontologyURI)) {
            if (!this.reusedVocabularies.contains(termNS)) {
                this.reusedVocabularies.add(termNS);
            }
        }else{
            //get label/def coverage for the ontology URI considered
            terms.add(a.getIRI().getIRIString());
            OWLAnnotationProperty label = ontologyModel.getOWLOntologyManager().getOWLDataFactory().getRDFSLabel();
            EntitySearcher.getAnnotations((OWLEntity) a, this.getOntologyModel(), label).forEach(ann -> {
                OWLAnnotationValue val = ann.getValue();
                if(val instanceof OWLLiteral) {
                    //System.out.println(" label " + ((OWLLiteral) val).getLiteral());
                    this.termsWithLabel.add(((OWLLiteral) val).getLiteral());
                }
            });
            OWLAnnotationProperty description = ontologyModel.getOWLOntologyManager().getOWLDataFactory().getRDFSComment();
            EntitySearcher.getAnnotations((OWLEntity) a, this.getOntologyModel(), description).forEach(ann -> {
                OWLAnnotationValue val = ann.getValue();
                if (val instanceof OWLLiteral) {
                    this.termsWithDescription.add(((OWLLiteral) val).getLiteral());
                }
            });
        }
    }

    public OWLOntology getOntologyModel() {
        return ontologyModel;
    }

    public String getOntologyURI() {
        return ontologyURI;
    }

    public Document getHtmlDocumentation() {
        return htmlDocumentation;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }

    public String getBackwardCompatibility() {
        return backwardCompatibility;
    }

    public String getCitation() {
        return citation;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getDoi() {
        return doi;
    }

    public String getLicense() {
        return license;
    }

    public String getLogo() {
        return logo;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public String getPreviousVersion() {
        return previousVersion;
    }

    public String getVersionIRI() {
        return versionIRI;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getSource() {
        return source;
    }

    public ArrayList<String> getSupportedMetadata() {
        return supportedMetadata;
    }

    public ArrayList<String> getReusedMetadataVocabularies() {
        return reusedMetadataVocabularies;
    }

    public List<OWLImportsDeclaration> getImportedVocabularies() {
        return importedVocabularies;
    }

    public ArrayList<String> getReusedVocabularies() {
        return reusedVocabularies;
    }

    public ArrayList<String> getTermsWithLabel() {
        return termsWithLabel;
    }

    public ArrayList<String> getTermsWithDescription() {
        return termsWithDescription;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }
}
