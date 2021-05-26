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

import java.util.ArrayList;

public class Constants {
    public static final String[] POSSIBLE_VOCAB_SERIALIZATIONS = { "application/rdf+xml", "text/turtle", "text/n3",
            "application/ld+json" };
    public static final String TEXT_HTML = "text/html";
    public static final String DEFAULT_OUT_PATH = "validation.json";
    public static final String HELP_TEXT ="java -jar fair_ontologies-version-jar-with-dependencies.jar [OPTIONS]\n"+
            "    -ontFile PATH  [required (unless -ontURI is used)]: Load a local ontology file (from PATH) " +
            "to document.\n "
            + "        This option is incompatible with -ontURI\n" +
            "    -ontURI  URI   [required (unless -ontFile is used)]: Load an ontology to document from its URI.\n"
            + "        This option is incompatible with -ontFile\n" ;

    /*Internal ids of checks and their explanations and descriptions*/
    // CN1: Content negotiation
    public static final String CN1 = "CN1";
    public static final String CN1_DESC = "Checks if the ontology URI is published following the right content " +
            "negotiation for RDF and HTML";
    public static final String CN1_DESC_EXPLANATION_OK = "Ontology available in: ";
    public static final String CN1_DESC_EXPLANATION_ERROR = "Ontology not available in RDF or HTML";

    //DOC1: HTML doc
    public static final String DOC1 = "DOC1";
    public static final String DOC1_DESC = "Check if the ontology has an HTML documentation";
    public static final String DOC1_EXPLANATION_OK = "Ontology available in HTML";
    public static final String DOC1_EXPLANATION_ERROR = "Ontology not available in HTML";

    //RDF1: Check if there is aRDF serialization of an ontology
    public static final String RDF1 = "RDF1";
    public static final String RDF1_DESC = "Check if the ontology has an RDF serialization";
    public static final String RDF1_EXPLANATION_OK = "Ontology available in RDF";
    public static final String RDF1_EXPLANATION_ERROR = "Ontology not available in RDF (RDF/XML, Turtle, JSON-LD or N3)";


    //PURL1: Use of persistent URIs
    public static final String PURL1 = "PURL1";
    public static final String PURL1_DESC = " Check if the ontology uses a persistent URL";
    public static final String PURL1_EXPLANATION_OK = "Ontology URI is persistent (w3id, purl, DOI, or a W3C URL)";
    public static final String PURL1_EXPLANATION_ERROR = "Ontology URI is not using a persistent id. " +
            "We checked w3id, purl, DOI and W3C";

    // Ontology metadata
    //minimum
    public static final String OM1 = "OM1";
    public static final String OM1_DESC = "Check to see is the following  minimum metadata [title, description, " +
            "license, version iri, creator, creationDate, namespace URI] are present";
    public static final String OM1_EXPLANATION = "The following metadata was not found: ";

    //recommended
    public static final String OM2 = "OM2";
    public static final String OM2_DESC = "Check to see if the following recommended metadata [NS Prefix, version info, " +
            "contributor, creation date, citation] are present";
    public static final String OM2_EXPLANATION = OM1_EXPLANATION;

    //optional
    public static final String OM3 = "OM3";
    public static final String OM3_DESC = "Check to see if the following optional metadata [doi, previous version," +
            "publisher, logo, backwards compatibility, status, modified, source, issued date] are found";
    public static final String OM3_EXPLANATION = OM1_EXPLANATION;

    //license
    public static final String OM4_1 = "OM4.1";
    public static final String OM4_1_DESC = "Check to see if there is a license associated with the ontology";
    public static final String OM4_1_EXPLANATION_OK = "A license was found";
    public static final String OM4_1_EXPLANATION_ERROR = "License not found";

    //license_resolvable
    public static final String OM4_2 = "OM4.2";
    public static final String OM4_2_DESC = "Check to see if the license is resolvable";
    public static final String OM4_2_EXPLANATION_OK = "License could be resolved";
    public static final String OM4_2_EXPLANATION_ERROR = "The license used could not be resolved";

    //provenance
    public static final String OM_5 = "OM5";
    public static final String OM5_DESC = "";
    public static final String OM5_EXPLANATION_OK = "";
    public static final String OM5_EXPLANATION_ERROR = "";

    /* FAIR Categories*/
    public static final String FINDABLE = "Findable";
    public static final String ACCESSIBLE = "Accessible";
    public static final String INTEROPERABLE = "Interoperable";
    public static final String REUSABLE = "Reusable";

    /* Status texts*/
    public static final String OK = "ok";
    public static final String ERROR = "error";

    /* Explanations for all */
    public static final String OK_TEST = "All tests have passed successfully";

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
    public static final String NS_FOAF = "http://xmlns.com/foaf/0.1/";

    public static final String PROP_FOAF_LOGO = NS_FOAF + "logo";

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
    public static final String PROP_SCHEMA_PUBLISHER = NS_SCHEMA + "publisher";
    public static final String PROP_SCHEMA_SCHEMA_VERSION = NS_SCHEMA + "schemaVersion";
    public static final String PROP_SCHEMA_SCHEMA_LOGO = NS_SCHEMA + "logo";

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
    public static final String PROP_DC_SOURCE = NS_DC + "source";

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
    public static final String PROP_DCTERMS_ISSUED = NS_DCTERMS + "issued";
    public static final String PROP_DCTERMS_SOURCE = NS_DCTERMS + "source";

    public static final String PROP_BIBO_DOI = NS_BIBO + "doi";
    public static final String PROP_BIBO_STATUS = NS_BIBO + "status";

    public static final String PROP_PROV_WAS_REVISION_OF = NS_PROV + "wasRevisionOf";
    public static final String PROP_PROV_GENERATED_AT_TIME = NS_PROV + "generatedAtTime";
    public static final String PROP_PROV_ATTRIBUTED_TO = NS_PROV + "wasAttributedTo";

    public static final String PROP_VANN_PREFIX = NS_VANN + "preferredNamespacePrefix";
    public static final String PROP_VANN_URI = NS_VANN + "preferredNamespaceUri";

    public static final String PROP_SKOS_NOTE = NS_SKOS + "note";

    public static final String PROP_PAV_CREATED_BY = NS_PAV + "createdBy";
    public static final String PROP_PAV_CREATED_ON = NS_PAV + "createdOn";
    public static final String PROP_PAV_PREVIOUS_VERSION = NS_PAV + "previousVersion";
    public static final String PROP_PAV_CONTRIBUTED_BY = NS_PAV + "contributedBy";

    public static final String PROP_CC_LICENSE = "http://creativecommons.org/ns#license";

    /*metadata names*/
    public static final String FOOPS_TITLE = "title";
    public static final String FOOPS_DESCRIPTION = "description";
    public static final String FOOPS_LICENSE = "license";
    public static final String FOOPS_VERSION_IRI = "version iri";
    public static final String FOOPS_AUTHOR = "author";
    public static final String FOOPS_NS_URI = "namespace URI";

    public static final String FOOPS_NS_PREFIX = "namespace prefix";
    public static final String FOOPS_VERSION_INFO = "version info";
    public static final String FOOPS_CONTRIBUTOR = "contributor";
    public static final String FOOPS_STATUS = "status";
    public static final String FOOPS_PREVIOUS_VERSION = "previous version";
    public static final String FOOPS_CREATION_DATE = "creation date";
    public static final String FOOPS_B_COMPATIBILITY = "backwards compatibility";
    public static final String FOOPS_PUBLISHER = "publisher";
    public static final String FOOPS_CITATION = "citation";
    public static final String FOOPS_DOI = "doi";

    public static final String FOOPS_LOGO = "logo";
    public static final String FOOPS_MODIFIED = "modified";
    public static final String FOOPS_SOURCE = "source";
    public static final String FOOPS_ISSUED = "issued";


    // metadata (using local names to avoid problems)
    public static final String[] MINIMUM_METADATA = {FOOPS_TITLE, FOOPS_DESCRIPTION, FOOPS_LICENSE, FOOPS_VERSION_IRI,
            FOOPS_AUTHOR, FOOPS_NS_URI};

    public static final String[] RECOMMENDED_METADATA = {FOOPS_NS_PREFIX, FOOPS_VERSION_INFO, FOOPS_CONTRIBUTOR,
            FOOPS_CREATION_DATE, FOOPS_CITATION};

    public static final String[] OPTIONAL_METADATA = {FOOPS_DOI, FOOPS_PREVIOUS_VERSION, FOOPS_PUBLISHER, FOOPS_LOGO,
            FOOPS_B_COMPATIBILITY, FOOPS_STATUS, FOOPS_MODIFIED, FOOPS_SOURCE, FOOPS_ISSUED};


}
