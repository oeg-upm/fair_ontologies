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

import fair.Constants;
import fair.Utils;
import org.jsoup.nodes.Document;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;

public class Ontology {
    private OWLOntology ontologyModel;
    private Document htmlDocumentation;
    private static final Logger logger = LoggerFactory.getLogger(Ontology.class);

    //metadata attributes [recommended]
    private String ontologyURI;
    private String namespacePrefix;
    private String title;
    private String description;
    private String versionIRI;
    private String versionInfo;
    private ArrayList<String> authors;
    private ArrayList<String> contributors;
    private String license;

    //metadata attributes [optional]
    private String abstractText;
    private String status;
    private String previousVersion;
    private String creationDate;
    private String backwardCompatibility;
    private String publisher;
    private String citation;
    private String doi;
    private String logo;

    /**
     *
     * @param o ontology path or URI
     * @param isFromFile flag to indicate whether the ontology should be loaded from file or URL
     * @param tmpFolder temporary folder where to store the downloaded ontology
     */
    public Ontology(String o, boolean isFromFile, Path tmpFolder){
        //Download ontology (any serialization)
        try {
            this.ontologyModel = Utils.loadModelToDocument(o, isFromFile, tmpFolder.toString());
            this.ontologyURI = this.ontologyModel.getOntologyID().getOntologyIRI().get().toString();
        }catch(Exception e){
            logger.error("Could not obtain ontology");
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
        this.getOntologyMetadata();
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
        String valueLanguage;
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
                    this.abstractText = a.getValue().asLiteral().get().getLiteral();
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
                break;
            case Constants.PROP_OWL_VERSION_INFO:
            case Constants.PROP_SCHEMA_SCHEMA_VERSION:
                try {
                    this.versionInfo = a.getValue().asLiteral().get().getLiteral();
                } catch (Exception e) {
                    logger.error("Error while getting ontology abstract. No literal provided");
                }
                break;
            case Constants.PROP_VANN_PREFIX:
                this.namespacePrefix = Utils.getValueAsLiteralOrURI(a.getValue());
                break;
            case Constants.PROP_VANN_URI:
                value = Utils.getValueAsLiteralOrURI(a.getValue());
                if (!this.ontologyURI.equals(value)) {
                    logger.error("Ontology NS URI declared and annotated is not the same!");
                }
                break;
            case Constants.PROP_DCTERMS_LICENSE:
            case Constants.PROP_DC_RIGHTS:
            case Constants.PROP_SCHEMA_LICENSE:
            case Constants.PROP_CC_LICENSE:
                try {
                    this.license = Utils.getValueAsLiteralOrURI(a.getValue());
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
            case Constants.PROP_SCHEMA_PUBLISER:
                try {
                    value = Utils.getValueAsLiteralOrURI(a.getValue());
                    switch (propertyName) {
                        case Constants.PROP_DC_CONTRIBUTOR:
                        case Constants.PROP_DCTERMS_CONTRIBUTOR:
                        case Constants.PROP_SCHEMA_CONTRIBUTOR:
                        case Constants.PROP_PAV_CONTRIBUTED_BY:
                            this.contributors.add(value);
                            break;
                        case Constants.PROP_DC_CREATOR:
                        case Constants.PROP_DCTERMS_CREATOR:
                        case Constants.PROP_PAV_CREATED_BY:
                        case Constants.PROP_PROV_ATTRIBUTED_TO:
                        case Constants.PROP_SCHEMA_CREATOR:
                            this.authors.add(value);
                            break;
                        default:
                            this.publisher = value;
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
                this.creationDate = a.getValue().asLiteral().get().getLiteral();
                break;
//            case Constants.PROP_DCTERMS_MODIFIED:
//            case Constants.PROP_SCHEMA_DATE_MODIFIED:
//                value = a.getValue().asLiteral().get().getLiteral();
//                mainOntologyMetadata.setReleaseDate(value);
//                break;
            case Constants.PROP_DCTERMS_BIBLIOGRAPHIC_CIT:
            case Constants.PROP_SCHEMA_CITATION:
                this.citation = Utils.getValueAsLiteralOrURI(a.getValue());
                break;
            case Constants.PROP_BIBO_DOI:
                this.doi = Utils.getValueAsLiteralOrURI(a.getValue());
                break;
            case Constants.PROP_BIBO_STATUS:
                try {
                    this.status = a.getValue().asLiteral().get().getLiteral();
                } catch (Exception e) {
                    logger.error("Error while getting the status. No literal provided");
                }
                break;
            case Constants.PROP_OWL_BACKWARDS_COMPATIBLE:
                this.backwardCompatibility = Utils.getValueAsLiteralOrURI(a.getValue());
                break;
//            case Constants.PROP_OWL_INCOMPATIBLE:
//                value = Utils.getValueAsLiteralOrURI(a.getValue());
//                mainOntologyMetadata.setIncompatibleWith(value);
//                break;
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

    public String getAbstractText() {
        return abstractText;
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
}
