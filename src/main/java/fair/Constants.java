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

package fair;

public class Constants {
    public static final String[] POSSIBLE_VOCAB_SERIALIZATIONS = { "application/rdf+xml", "text/turtle", "text/n3",
            "application/ld+json" };
    public static final String TEXT_HTML = "text/html";
    public static final String DEFAULT_OUT_PATH = "validation.json";
    public static final String HELP_TEXT ="java -jar fair_ontologies-version-jar-with-dependencies.jar [OPTIONS]\n"+
            "    -ontFile PATH  [required (unless -ontURI is used)]: Load a local ontology file (from PATH) to document.\n "
            + "        This option is incompatible with -ontURI\n" +
            "    -ontURI  URI   [required (unless -ontFile is used)]: Load an ontology to document from its URI.\n"
            + "        This option is incompatible with -ontFile\n" ;

    /*Internal ids of checks and their explanations and descriptions*/
    // CN1: Content negotiation
    public static String CN1 = "CN1";
    public static final String CN1_DESC = "Checks if the ontology URI is published following the right content negotiation for RDF and HTML";
    public static final String CN1_DESC_EXPLANATION_OK = "Ontology available in: ";
    public static final String CN1_DESC_EXPLANATION_ERROR = "Ontology not available in RDF or HTML";

    //DOC1: HTML doc
    public static String DOC1 = "DOC1";
    public static final String DOC1_DESC = "Check if the ontology has an HTML documentation";
    public static final String DOC1_EXPLANATION_OK = "Ontology available in HTML";
    public static final String DOC1_EXPLANATION_ERROR = "Ontology not available in HTML";

    // PURL1: Use of persistent URIs
    public static String PURL1 = "PURL1";
    public static String PURL1_DESC = " Check if the ontology uses a persistent URL";
    public static final String PURL1_EXPLANATION_OK = "Ontology URI is w3id, purl or a W3C URL";
    public static final String PURL1_EXPLANATION_ERROR = "Ontology URI is not using a persistent id (purl, w3id, etc.)";

    // Ontology metadata
    public static String ONTO_METADATA = "OM1";

    /* FAIR Categories*/
    public static String FINDABLE = "Findable";
    public static String ACCESSIBLE = "Accessible";
    public static String INTEROPERABLE = "Interoperable";
    public static String REUSABLE = "Reusable";

    /* Status texts*/
    public static String OK = "ok";
    public static String ERROR = "error";

    /* Explanations for all */
    public static String OK_TEST = "All tests have passed successfully";

    /**
     * Constants for loading metadata properties from the ontology
     */

    public static final String NS_RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String NS_SCHEMA = "https://schema.org/";
    public static final String NS_DC = "http://purl.org/dc/elements/1.1/";
    public static final String NS_DCTERMS = "http://purl.org/dc/terms/";
    public static final String NS_OWL = "http://www.w3.org/2002/07/owl#";
    public static final String NS_VANN = "http://purl.org/vocab/vann/";
    // public static final String NS_VAEM =
    // "http://www.linkedmodel.org/schema/vaem#";
    public static final String NS_PROV = "http://www.w3.org/ns/prov#";
    public static final String NS_BIBO = "http://purl.org/ontology/bibo/";
    public static final String NS_SKOS = "http://www.w3.org/2004/02/skos/core#";
    public static final String NS_PAV = "http://purl.org/pav/";

    public static final String PROP_RDFS_LABEL = NS_RDFS + "label";
    public static final String PROP_RDFS_COMMENT = NS_RDFS + "comment";

    public static final String PROP_SCHEMA_NAME = NS_SCHEMA + "name";
    public static final String PROP_SCHEMA_CREATOR = NS_SCHEMA + "creator";
    public static final String PROP_SCHEMA_LICENSE = NS_SCHEMA + "license";
    public static final String PROP_SCHEMA_CONTRIBUTOR = NS_SCHEMA + "contributor";
    public static final String PROP_SCHEMA_DESCRIPTION = NS_SCHEMA + "description";
    public static final String PROP_SCHEMA_CITATION = NS_SCHEMA + "citation";
    public static final String PROP_SCHEMA_DATE_CREATED = NS_SCHEMA + "dateCreated";
    public static final String PROP_SCHEMA_DATE_MODIFIED = NS_SCHEMA + "dateModified";
    public static final String PROP_SCHEMA_PUBLISER = NS_SCHEMA + "publisher";
    public static final String PROP_SCHEMA_SCHEMA_VERSION = NS_SCHEMA + "schemaVersion";

    public static final String PROP_OWL_VERSION_INFO = NS_OWL + "versionInfo";
    public static final String PROP_OWL_PRIOR_VERSION = NS_OWL + "priorVersion";
    public static final String PROP_OWL_BACKWARDS_COMPATIBLE = NS_OWL + "backwardCompatibleWith";
    public static final String PROP_OWL_INCOMPATIBLE = NS_OWL + "incompatibleWith";

    public static final String PROP_DC_TITLE = NS_DC + "title";
    public static final String PROP_DC_RIGHTS = NS_DC + "rights";
    public static final String PROP_DC_ABSTRACT = NS_DC + "abstract";
    public static final String PROP_DC_DESCRIPTION = NS_DC + "description";
    public static final String PROP_DC_CREATOR = NS_DC + "creator";
    public static final String PROP_DC_REPLACES = NS_DC + "replaces";
    public static final String PROP_DC_CONTRIBUTOR = NS_DC + "contributor";
    public static final String PROP_DC_PUBLISHER = NS_DC + "publisher";

    public static final String PROP_DCTERMS_REPLACES = NS_DCTERMS + "replaces";
    public static final String PROP_DCTERMS_DESCRIPTION = NS_DCTERMS + "description";
    public static final String PROP_DCTERMS_LICENSE = NS_DCTERMS + "license";
    public static final String PROP_DCTERMS_TITLE = NS_DCTERMS + "title";
    public static final String PROP_DCTERMS_ABSTRACT = NS_DCTERMS + "abstract";
    public static final String PROP_DCTERMS_CREATOR = NS_DCTERMS + "creator";
    public static final String PROP_DCTERMS_CONTRIBUTOR = NS_DCTERMS + "contributor";
    public static final String PROP_DCTERMS_PUBLISHER = NS_DCTERMS + "publisher";
    public static final String PROP_DCTERMS_CREATED = NS_DCTERMS + "created";
    public static final String PROP_DCTERMS_MODIFIED = NS_DCTERMS + "modified";
    public static final String PROP_DCTERMS_BIBLIOGRAPHIC_CIT = NS_DCTERMS + "bibliographicCitation";

    public static final String PROP_BIBO_DOI = NS_BIBO + "doi";
    public static final String PROP_BIBO_STATUS = NS_BIBO + "status";

    public static final String PROP_PROV_WAS_REVISION_OF = NS_PROV + "wasRevisionOf";
    public static final String PROP_PROV_GENERATED_AT_TIME = NS_PROV + "generatedAtTime";
    public static final String PROP_PROV_ATTRIBUTED_TO = NS_PROV + "wasAttributedTo";

    public static final String PROP_VANN_PREFIX = NS_VANN + "preferredNamespacePrefix";
    public static final String PROP_VANN_URI = NS_VANN + "preferredNamespaceURI";

    public static final String PROP_SKOS_NOTE = NS_SKOS + "note";

    public static final String PROP_PAV_CREATED_BY = NS_PAV + "createdBy";
    public static final String PROP_PAV_CREATED_ON = NS_PAV + "createdOn";
    public static final String PROP_PAV_PREVIOUS_VERSION = NS_PAV + "previousVersion";
    public static final String PROP_PAV_CONTRIBUTED_BY = NS_PAV + "contributedBy";

    public static final String PROP_CC_LICENSE = "http://creativecommons.org/ns#license";

}
