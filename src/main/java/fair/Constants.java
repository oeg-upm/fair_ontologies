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
package fair;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Constants {

    public static final String CN1_URL = "https://w3id.org/foops/test/CN1";

    public static final String DOC1_URL = "https://w3id.org/foops/test/DOC1";

    public static final String FIND1_URL = "https://w3id.org/foops/test/FIND1";

    public static final String FIND2_URL = "https://w3id.org/foops/test/FIND2";

    public static final String FIND3_URL = "https://w3id.org/foops/test/FIND3";

    public static final String FIND3_BIS_URL = "https://w3id.org/foops/test/FIND_3_BIS";

    public static final String HTTP1_URL = "https://w3id.org/foops/test/HTTP1";

    public static final String OM1_URL = "https://w3id.org/foops/test/OM1";

    public static final String OM2_URL = "https://w3id.org/foops/test/OM2";

    public static final String OM3_URL= "https://w3id.org/foops/test/OM3";

    public static final String OM4_1_URL = "https://w3id.org/foops/test/OM4.1";

    public static final String OM4_2_URL = "https://w3id.org/foops/test/OM4.2";

    public static final String OM5_1_URL = "https://w3id.org/foops/test/OM5.1";

    public static final String OM5_2_URL = "https://w3id.org/foops/test/OM5.2";

    public static final String PURL1_URL = "https://w3id.org/foops/test/PURL1";

    public static final String URI1_URL = "https://w3id.org/foops/test/URI1";

    public static final String URI2_URL = "https://w3id.org/foops/test/URI2";

    public static final String VER1_URL = "https://w3id.org/foops/test/VER1";

    public static final String VER2_URL = "https://w3id.org/foops/test/VER2";

    public static final String VOC1_URL = "https://w3id.org/foops/test/VOC1";

    public static final String VOC2_URL = "https://w3id.org/foops/test/VOC2";

    public static final String VOC3_URL = "https://w3id.org/foops/test/VOC3";

    public static final String VOC4_URL = "https://w3id.org/foops/test/VOC4";

    public static final String RDF1_URL = "https://w3id.org/foops/test/RDF1";

    private static final Logger log = LoggerFactory.getLogger(Constants.class);

    // CN1: Content negotiation
    public static final String CN1 = "CN1";
    public static String CN1_TITLE;
    public static String CN1_DESC;
    public static final String CN1_DESC_EXPLANATION_OK = "Ontology available in: ";
    public static final String CN1_DESC_EXPLANATION_ERROR = "Ontology not available in RDF or HTML";
    //DOC1: HTML doc
    public static final String DOC1 = "DOC1";
    public static String DOC1_TITLE;
    public static String DOC1_DESC; // = "This check verifies if the ontology has an HTML documentation";
    public static final String DOC1_EXPLANATION_OK = "Ontology available in HTML";
    public static final String DOC1_EXPLANATION_ERROR = "Ontology not available in HTML";

    //RDF1: Check if there is an RDF serialization of an ontology
    public static final String RDF1 = "RDF1";
    public static String RDF1_TITLE;// = "RDF Availability";
    public static String RDF1_DESC;// = "This check verifies if the ontology has an RDF serialization " +
            //"(ttl, n3, rdf/xml, json-ld)";
    public static final String RDF1_EXPLANATION_OK = "Ontology available in RDF";
    public static final String RDF1_EXPLANATION_ERROR = "Ontology not available in RDF (RDF/XML, Turtle, JSON-LD or N3)";

    //PURL1: Use of persistent URIs
    public static final String PURL1 = "PURL1";
    public static String PURL1_TITLE;// = "Persistent URL";
    public static String PURL1_DESC;// = " This check verifies if the ontology has a persistent URL " +
            //"(w3id, purl, DOI, or a W3C URL)";
    public static final String PURL1_EXPLANATION_OK = "Ontology URI is persistent";
    public static final String PURL1_EXPLANATION_ERROR = "the ontology URI does not follow any of the schemes " +
            "followed by known registers of persistent URIs. " +
            "We checked w3id, purl, DOI, W3C, perma.cc, linked.data.gov.au, data.europa.eu and dbpedia.org";

    // Ontology metadata
    //minimum
    public static final String OM1 = "OM1";
    public static String OM1_TITLE; //= "Minimum metadata";
    public static String OM1_DESC; //= "This check verifies if the The following  minimum metadata " +
            //"[title, description, license, version iri, creator, creationDate, namespace URI] " +
           // "are present in the ontology";
    public static final String OM1_EXPLANATION_OK = "All the minimum metadata were found!";
    public static final String OM1_EXPLANATION_ERROR = "The following metadata was not found: ";

    //recommended
    public static final String OM2 = "OM2";
    public static String OM2_TITLE; // = "Recommended metadata";
    public static String OM2_DESC;  //= "This check verifies if the following recommended metadata " +
            //"[NS Prefix, version info, creation date, citation] are present in the ontology. " +
            //"It also checks if [contributor] is "  +
            //"present, but with no penalty (as no all ontologies may have a contributor)";
    public static final String OM2_EXPLANATION = OM1_EXPLANATION_ERROR;

    //optional
    public static final String OM3 = "OM3";
    public static String OM3_TITLE; // = "Detailed metadata";
    public static String OM3_DESC; //= "This check verifies if the following detailed metadata " +
            //"[doi, publisher, logo, status, source, issued date] " +
            //"are present in the ontology. It also checks if [previous version, backward compatibility, modified] are " +
            //"present, but with no penalty (as no all ontologies may have, e.g., a previous version)";
    public static final String OM3_EXPLANATION = OM1_EXPLANATION_ERROR;

    //license
    public static final String OM4_1 = "OM4_1";
    public static String OM4_1_TITLE; // = "License availability";
    public static String OM4_1_DESC; // = "This check verifies if a license associated with the ontology";
    public static final String OM4_1_EXPLANATION_OK = "A license was found";
    public static final String OM4_1_EXPLANATION_OK_RIGHTS = "A license was not found, but we found a rights statement: ";
    public static final String OM4_1_EXPLANATION_ERROR = "License or rights not found";

    //license_resolvable
    public static final String OM4_2 = "OM4_2";
    public static String OM4_2_TITLE; // = "License is resolvable";
    public static String OM4_2_DESC; // = "This check verifies if the ontology license is resolvable";
    public static final String OM4_2_EXPLANATION_OK = "License could be resolved";
    public static final String OM4_2_EXPLANATION_ERROR = "The license used could not be resolved";

    //provenance
    public static final String OM5_1 = "OM5_1";
    public static String OM5_1_TITLE; // = "Basic provenance metadata";
    public static String OM5_1_DESC; // = "This check verifies if basic provenance is available for the ontology: " +
    // "[author, creation date]. This check also verifies whether [contributor, previous version] are present" +
    //         ", but with no penalty (as no all ontologies may have a previous version or a contributor)";
    public static final String OM5_1_EXPLANATION = "The following provenance information was not found: ";

    public static final String OM5_2 = "OM5_2";
    public static String OM5_2_TITLE; // = "Detailed provenance metadata";
    public static String OM5_2_DESC; // = "This check verifies if detailed provenance information is available " +
    // "for the ontology: [issued date, publisher]";
    public static final String OM5_2_EXPLANATION= OM5_1_EXPLANATION;

    //Findability
    public static final String FIND1 = "FIND1";
    public static String FIND1_TITLE; //  = "Ontology prefix";
    public static String FIND1_DESC; //  = "This check verifies if an ontology prefix is available";
    public static final String FIND1_EXPLANATION_OK= "Prefix declaration found in the ontology";
    public static final String FIND1_EXPLANATION_ERROR= "Prefix declaration not found in the ontology";

    public static final String FIND2 = "FIND2";
    public static String FIND2_TITLE; //  = "Prefix is in registry";
    public static String FIND2_DESC; //  = "This check verifies if the ontology prefix can be " +
    // "found in prefix.cc or LOV registries. This check also verifies if the prefix resolves to the same namespace" +
    //         "prefix found in the ontology.";
    public static final String FIND2_EXPLANATION_OK= "Prefix declaration found with correct namespace";
    public static final String FIND2_EXPLANATION_OK_ALMOST= "Prefix declaration found, but with incorrect namespace";
    public static final String FIND2_EXPLANATION_ERROR= "Prefix declaration not found in prefix.cc or LOV";

    public static final String FIND3 = "FIND3";
    public static String FIND3_TITLE;//  = "Ontology in metadata registry";
    public static String FIND3_DESC;//  = "This check verifies if the ontology can be found in a public registry (LOV)";
    public static final String FIND3_EXPLANATION_OK= "Ontology namespace found in";
    public static final String FIND3_EXPLANATION_ERROR= "Ontology not found in a public registry";

    public static final String FIND3_BIS = "FIND_3_BIS";
    public static String FIND3_BIS_TITLE;//  = "Metadata are accessible, even when ontology is not";
    public static String FIND3_BIS_DESC;//  = "Metadata are accessible even when the " +
    // "ontology is no longer available. Since the metadata is usually included in the ontology, this check " +
    //      "verifies whether the ontology is registered in a public metadata registry (LOV)";

    //Access protocol
    public static final String HTTP1 = "HTTP1";
    public static String HTTP1_TITLE;//  = "Open protocol";
    public static String HTTP1_DESC;//  = "This check verifies if the ontology uses an open " +
    // "protocol (HTTP or HTTPS)";
    public static final String HTTP1_EXPLANATION_OK= "The ontology uses an open protocol";
    public static final String HTTP1_EXPLANATION_ERROR= "The ontology does not use an open protocol";

    //URI1: URI is resolvable
    public static final String URI1 = "URI1";
    public static String URI1_TITLE;// = "Ontology URI is resolvable";
    public static String URI1_DESC;// = "This check verifies if the ontology URI found within the ontology " +
    //"document is resolvable";
    public static final String URI1_EXPLANATION_OK = "Ontology URL is resolvable";
    public static final String URI1_EXPLANATION_ERROR = "Ontology URL is not resolvable";

    //URI2: Ontology URI is the URI used (only if ontology was loaded through URI)
    public static final String URI2 = "URI2";
    public static String URI2_TITLE; //= "Consistent ontology IDs";
    public static String URI2_DESC; //= "This check verifies if the ontology URI is equal to the ontology ID";
    public static final String URI2_EXPLANATION_OK = "Ontology URI is equal to ontology id";
    public static final String URI2_EXPLANATION_ERROR_LOAD = "The ontology could not be loaded, so an ontology ID " +
            "was not found.";
    public static final String URI2_EXPLANATION_ERROR = "Ontology URI is different from ontology ID. Your ontology" +
            "URI (e.g., its w3id) should be the same as the one used within the ontology itself";

    //version IRI tests
    public static final String VER1 = "VER1";
    public static String VER1_TITLE;// = "Version IRI";
    public static String VER1_DESC;// = "This check verifies if there is an id for this ontology version, and " +
            //"whether the id is unique (i.e., different from the ontology URI)";
    public static final String VER1_EXPLANATION_OK= "Version IRI defined, IRI is different from ontology URI";
    public static final String VER1_EXPLANATION_ERROR= "Version IRI "; // to complete on whether it's found or duplicate

    public static final String VER2 = "VER2";
    public static String VER2_TITLE;// = "Version IRI resolves";
    public static String VER2_DESC;// = "This check verifies if the version IRI resolves";
    public static final String VER2_EXPLANATION_OK= "Version IRI resolves";
    public static final String VER2_EXPLANATION_ERROR_NOT_AVAILABLE= "Version IRI is not available, " +
            "so it could not be resolved";
    public static final String VER2_EXPLANATION_ERROR= "Version IRI could not be resolved";

    //reuse
    public static final String VOC1 = "VOC1";
    public static String VOC1_TITLE;//  = "Vocabulary reuse (metadata)";
    public static String VOC1_DESC; //  = "This check verifies if the ontology reuses other vocabularies for " +
    // "declaring metadata terms";
    public static final String VOC1_EXPLANATION_OK = "Ontology reuses existing vocabularies for declaring metadata. ";
    public static final String VOC1_EXPLANATION_ERROR = "The ontology does not reuse vocabularies for common metadata";

    public static final String VOC2 = "VOC2";
    public static String VOC2_TITLE;//  = "Vocabulary reuse";
    public static String VOC2_DESC;//  = "This check verifies if the ontology imports/extends other vocabularies " +
    // "(besides RDF, OWL and RDFS)";
    public static final String VOC2_EXPLANATION_OK_IMPORT = "The ontology imports the following vocabularies: ";
    public static final String VOC2_EXPLANATION_OK_EXTEND = "The ontology reuses/extends existing vocabularies.";
    public static final String VOC2_EXPLANATION_ERROR = "The ontology does not import/extend other vocabularies.";

    public static final String VOC3 = "VOC3";
    public static String VOC3_TITLE;//  = "Documentation: labels";
    public static String VOC3_DESC;//  = "This check verifies the extent to which all ontology terms have " +
    // "labels (rdfs:label in OWL vocabularies, skos:prefLabel in SKOS vocabularies)";
    public static final String VOC3_EXPLANATION_OK = "Labels found for all ontology terms";
    public static final String VOC3_EXPLANATION_ERROR = "Labels found for "; // percentage

    public static final String VOC4 = "VOC4";
    public static String VOC4_TITLE;// = "Documentation: definitions";
    public static String VOC4_DESC;// = "This check verifies whether all ontology terms have " +
    //"descriptions (rdfs:comment in OWL vocabularies, skos:definition in SKOS vocabularies)";
    public static final String VOC4_EXPLANATION_OK= "Descriptions found for all ontology terms";
    public static final String VOC4_EXPLANATION_ERROR= "Descriptions found for "; // percentage
    static{
        Properties props = new Properties();
        ClassLoader classLoader = Constants.class.getClassLoader();
        try (InputStream fis = classLoader.getResourceAsStream("testCatalogue.properties")) {
            props.load(fis);
            CN1_TITLE = props.getProperty("CN1.name");
            CN1_DESC = props.getProperty("CN1.description");
            DOC1_TITLE =  props.getProperty("DOC1.name");
            DOC1_DESC =  props.getProperty("DOC1.description");
            FIND1_TITLE =  props.getProperty("FIND1.name");
            FIND1_DESC =  props.getProperty("FIND1.description");
            FIND2_TITLE =  props.getProperty("FIND2.name");
            FIND2_DESC =  props.getProperty("FIND2.description");
            FIND3_TITLE =  props.getProperty("FIND3.name");
            FIND3_DESC =  props.getProperty("FIND3.description");
            FIND3_BIS_TITLE =  props.getProperty("FIND_3_BIS.name");
            FIND3_BIS_DESC =  props.getProperty("FIND_3_BIS.description");
            HTTP1_TITLE =  props.getProperty("HTTP1.name");
            HTTP1_DESC =  props.getProperty("HTTP1.description");
            OM1_TITLE =  props.getProperty("OM1.name");
            OM1_DESC =  props.getProperty("OM1.description");
            OM2_TITLE =  props.getProperty("OM2.name");
            OM2_DESC =  props.getProperty("OM2.description");
            OM3_TITLE =  props.getProperty("OM3.name");
            OM3_DESC =  props.getProperty("OM3.description");
            OM4_1_TITLE =  props.getProperty("OM4.1.name");
            OM4_1_DESC =  props.getProperty("OM4.1.description");
            OM4_2_TITLE =  props.getProperty("OM4.2.name");
            OM4_2_DESC =  props.getProperty("OM4.2.description");
            OM5_1_TITLE =  props.getProperty("OM5.1.name");
            OM5_1_DESC =  props.getProperty("OM5.1.description");
            OM5_2_TITLE =  props.getProperty("OM5.2.name");
            OM5_2_DESC =  props.getProperty("OM5.2.description");
            PURL1_TITLE = props.getProperty("PURL1.name");
            PURL1_DESC = props.getProperty("PURL1.description");
            RDF1_TITLE = props.getProperty("RDF1.name");
            RDF1_DESC = props.getProperty("RDF1.description");
            URI1_TITLE = props.getProperty("URI1.name");
            URI1_DESC = props.getProperty("URI1.description");
            URI2_TITLE = props.getProperty("URI2.name");
            URI2_DESC = props.getProperty("URI2.description");
            VER1_TITLE = props.getProperty("VER1.name");
            VER1_DESC = props.getProperty("VER1.description");
            VER2_TITLE = props.getProperty("VER2.name");
            VER2_DESC = props.getProperty("VER2.description");
            VOC1_TITLE = props.getProperty("VOC1.name");
            VOC1_DESC = props.getProperty("VOC1.description");
            VOC2_TITLE = props.getProperty("VOC2.name");
            VOC2_DESC = props.getProperty("VOC2.description");
            VOC3_TITLE = props.getProperty("VOC3.name");
            VOC3_DESC = props.getProperty("VOC3.description");
            VOC4_TITLE = props.getProperty("VOC4.name");
            VOC4_DESC = props.getProperty("VOC4.description");
        }catch(Exception e){
            log.error("Error while retrieving test descriptions from .properties file!");
        }
    }

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

    // registries
    public static final String LOV_ALL_VOCABS = "https://lov.linkeddata.es/dataset/lov/api/v2/vocabulary/list";
    public static final String LOV_PREFIX_VOCAB = "https://lov.linkeddata.es/dataset/lov/api/v2/vocabulary/info?vocab=";
    public static final String PREFIX_CC = "http://prefix.cc/";

    //to do: ontobee (http://www.ontobee.org/sparql), bioportal,


    /* FAIR Categories*/
    public static final String FINDABLE = "Findable";
    public static final String ACCESSIBLE = "Accessible";
    public static final String INTEROPERABLE = "Interoperable";
    public static final String REUSABLE = "Reusable";

    /* Status texts*/
    public static final String OK = "ok";
    public static final String ERROR = "error";

    public static final String ERROR_METADATA = "Metadata could not be loaded";
    public static final String ERROR_VOC = "Could not find any imported/reused vocabularies";

    /**
     * Constants for loading metadata properties from the ontology
     */

    public static final String NS_RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String NS_SCHEMA = "https://schema.org/";
    public static final String NS_SCHEMA_HTTP = "http://schema.org/";
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
    public static final String NS_CC = "http://creativecommons.org/ns#";
    public static final String NS_DOAP = "http://usefulinc.com/ns/doap#";

    public static final String[] VOCS_REUSE_METADATA = {NS_DC, NS_SCHEMA, NS_DCTERMS, NS_VANN, NS_PROV,
            NS_BIBO, NS_PAV, NS_FOAF, NS_RDFS, NS_OWL, NS_DOAP };


    public static final String SKOS_CONCEPT = NS_SKOS + "Concept";
    public static final String SKOS_CONCEPT_SCHEME = NS_SKOS +  "ConceptScheme";
    public static final String PROP_SKOS_PREF_LABEL = NS_SKOS +  "prefLabel";
    public static final String PROP_SKOS_PREF_DEFINITION = NS_SKOS +  "definition";

    public static final String PROP_OBO_ALT_LABEL = "http://purl.obolibrary.org/obo/IAO_0000118";

    public static final String PROP_FOAF_LOGO = NS_FOAF + "logo";
    public static final String PROP_FOAF_MAKER = NS_FOAF + "maker";
    public static final String PROP_FOAF_DEPICTION = NS_FOAF + "depiction";
    public static final String PROP_FOAF_HOME_PAGE = NS_FOAF + "homePage";

    public static final String PROP_DOAP_SHORT_DESC = NS_DOAP + "shortdesc";
    public static final String PROP_DOAP_DESCRIPTION = NS_DOAP + "description";
    public static final String PROP_DOAP_MAINTAINER = NS_DOAP + "maintainer";
    public static final String PROP_DOAP_DEVELOPER = NS_DOAP + "developer";
    public static final String PROP_DOAP_DOCUMENTER = NS_DOAP + "documenter";
    public static final String PROP_DOAP_TRANSLATOR = NS_DOAP + "translator";
    public static final String PROP_DOAP_TESTER = NS_DOAP + "tester";
    public static final String PROP_DOAP_HELPER = NS_DOAP + "helper";
    public static final String PROP_DOAP_LICENSE = NS_DOAP + "license";
    public static final String PROP_DOAP_NAME = NS_DOAP + "name";
    public static final String PROP_DOAP_CREATED = NS_DOAP + "created";

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
    public static final String PROP_SCHEMA_IDENTIFIER = NS_SCHEMA + "identifier";
    public static final String PROP_SCHEMA_PUBLISHER = NS_SCHEMA + "publisher";
    public static final String PROP_SCHEMA_SCHEMA_VERSION = NS_SCHEMA + "schemaVersion";
    public static final String PROP_SCHEMA_LOGO = NS_SCHEMA + "logo";
    public static final String PROP_SCHEMA_DATE_PUBLISHED = NS_SCHEMA + "datePublished";
    public static final String PROP_SCHEMA_DATE_RELEASED = NS_SCHEMA + "releaseDate";

    public static final String PROP_SCHEMA_NAME_HTTP = NS_SCHEMA_HTTP + "name";
    public static final String PROP_SCHEMA_CREATOR_HTTP = NS_SCHEMA_HTTP + "creator";
    public static final String PROP_SCHEMA_LICENSE_HTTP = NS_SCHEMA_HTTP + "license";
    public static final String PROP_SCHEMA_CONTRIBUTOR_HTTP = NS_SCHEMA_HTTP + "contributor";
    public static final String PROP_SCHEMA_DESCRIPTION_HTTP = NS_SCHEMA_HTTP + "description";
    public static final String PROP_SCHEMA_CITATION_HTTP = NS_SCHEMA_HTTP + "citation";
    public static final String PROP_SCHEMA_DATE_CREATED_HTTP = NS_SCHEMA_HTTP + "dateCreated";
    public static final String PROP_SCHEMA_DATE_MODIFIED_HTTP = NS_SCHEMA_HTTP + "dateModified";
    public static final String PROP_SCHEMA_IDENTIFIER_HTTP = NS_SCHEMA_HTTP + "identifier";
    public static final String PROP_SCHEMA_PUBLISHER_HTTP = NS_SCHEMA_HTTP + "publisher";
    public static final String PROP_SCHEMA_SCHEMA_VERSION_HTTP = NS_SCHEMA_HTTP + "schemaVersion";
    public static final String PROP_SCHEMA_LOGO_HTTP = NS_SCHEMA_HTTP + "logo";
    public static final String PROP_SCHEMA_DATE_PUBLISHED_HTTP = NS_SCHEMA_HTTP + "datePublished";
    public static final String PROP_SCHEMA_DATE_RELEASED_HTTP = NS_SCHEMA_HTTP + "releaseDate";

    public static final String PROP_OWL_VERSION_INFO = NS_OWL + "versionInfo";

    public static final String PROP_OWL_VERSION_IRI = NS_OWL + "versionIRI";
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

    public static final String PROP_DCTERMS_RIGHTS = NS_DCTERMS + "rights";
    public static final String PROP_DCTERMS_ACCESS_RIGHTS = NS_DCTERMS + "accessRights";
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
    public static final String PROP_DCTERMS_SUBMITTED = NS_DCTERMS + "submitted";
    public static final String PROP_DCTERMS_IDENTIFIER = NS_DCTERMS + "identifier";
    public static final String PROP_DCTERMS_SOURCE = NS_DCTERMS + "source";

    public static final String PROP_BIBO_DOI = NS_BIBO + "doi";
    public static final String PROP_BIBO_STATUS = NS_BIBO + "status";

    public static final String PROP_PROV_WAS_REVISION_OF = NS_PROV + "wasRevisionOf";
    public static final String PROP_PROV_GENERATED_AT_TIME = NS_PROV + "generatedAtTime";
    public static final String PROP_PROV_ATTRIBUTED_TO = NS_PROV + "wasAttributedTo";
    public static final String PROP_PROV_HAD_ORIGINAL_SOURCE = NS_PROV + "hadOriginalSource";

    public static final String PROP_VANN_PREFIX = NS_VANN + "preferredNamespacePrefix";
    public static final String PROP_VANN_URI = NS_VANN + "preferredNamespaceUri";

    public static final String PROP_SKOS_NOTE = NS_SKOS + "note";

    public static final String PROP_PAV_CREATED_BY = NS_PAV + "createdBy";
    public static final String PROP_PAV_AUTHORED_BY = NS_PAV + "authoredBy";
    public static final String PROP_PAV_CREATED_ON = NS_PAV + "createdOn";
    public static final String PROP_PAV_PREVIOUS_VERSION = NS_PAV + "previousVersion";
    public static final String PROP_PAV_CONTRIBUTED_BY = NS_PAV + "contributedBy";

    public static final String PROP_CC_LICENSE = NS_CC + "license";

    /*metadata names*/
    public static final String FOOPS_TITLE = "title";
    public static final String FOOPS_DESCRIPTION = "description";
    public static final String FOOPS_LICENSE = "license";
    public static final String FOOPS_RIGHTS = "rights";
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
    public static final String FOOPS_NAME = "name";

    public static final String BENCHMARK_ALL_NAME = "General Benchmark for FAIR Principles";
    public static final String BENCHMARK_ALL_DESCRIPTION = "Set of test results that includes all tests included in FOOPS! for FAIR assessment of ontologies.";
    public static final String BENCHMARK_PRE_NAME = "Pre-assessment benchmark for FAIR principles";
    public static final String BENCHMARK_PRE_DESCRIPTION = "Set of test results that includes tests for files and ontology code. No accessibility tests are performed, since this is meant for aiding researchers that have not made their ontologies available online yet. ";


    // metadata (using local names to avoid problems)
    public static final String[] MINIMUM_METADATA = {FOOPS_TITLE, FOOPS_DESCRIPTION, FOOPS_LICENSE, FOOPS_VERSION_IRI,
            FOOPS_AUTHOR, FOOPS_NS_URI};

    public static final String[] RECOMMENDED_METADATA = {FOOPS_NS_PREFIX, FOOPS_VERSION_INFO,
            FOOPS_CREATION_DATE, FOOPS_CITATION};

    public static final String[] RECOMMENDED_METADATA_OPTIONAL = {FOOPS_CONTRIBUTOR};

    public static final String[] DETAILED_METADATA = {FOOPS_DOI , FOOPS_PUBLISHER, FOOPS_LOGO, FOOPS_STATUS,
            FOOPS_SOURCE, FOOPS_ISSUED};

    public static final String[] DETAILED_METADATA_OPTIONAL = {FOOPS_PREVIOUS_VERSION, FOOPS_B_COMPATIBILITY,
            FOOPS_MODIFIED,};

    public static final String[] PROVENANCE_METADATA_BASIC = {FOOPS_CREATION_DATE, FOOPS_AUTHOR};

    public static final String[] PROVENANCE_METADATA_OPTIONAL = {FOOPS_CONTRIBUTOR, FOOPS_PREVIOUS_VERSION};

    public static final String[] PROVENANCE_METADATA_DETAILED = {FOOPS_ISSUED, FOOPS_PUBLISHER};

    public static final String[] FOUNDATIONAL_ONTOLOGIES = {
            "http://www.ifomis.org/bfo/1.1#",
            "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl",
            "http://emmo.info/emmo/top#",
            "http://sw.cyc.com/2006/07/27/cyc/",
            "http://www.onto-med.de/ontologies/gfo.owl#",
            "http://www.onto-med.de/ontologies/gfo-basic.owl#",
            "https://schema.org/",
            "http://schema.org/",
            "http://www.hozo.jp/owl/YAMATO20210604.miz.owl#"
    };

    /*
    * Constants for exporting test results in JSON-LD. The default JSON-LD export from libraries
    * is really hard to read, so this simplified version with context aims to aid this
    * process. A templating approach was chosen.
    * */
    public static String FTR_CONTEXT = " \"@context\":\"https://w3id.org/ftr/context\",\n";
    public static String JSON_LD_TEST_CONTENT_REDUCED =
            "\n" + FTR_CONTEXT +
            "    \"@id\": \"$RESULT_ID\",\n" +
            "    \"@type\": \"https://w3id.org/ftr#TestResult\",\n" +
            "    \"description\": \"$RESULT_DESCRIPTION\",\n" +
            "    \"identifier\": {\n" +
            "        \"@id\": \"$RESULT_ID\"\n" +
            "    },\n" +
            "      \"license\": {\n" +
            "        \"@id\": \"http://creativecommons.org/licenses/by/4.0/\"\n" +
            "      },\n" +
            "    \"title\": \"$RESULT_TITLE\",\n" +
            "    \"value\": \"$RESULT_VALUE\",\n" +
            "    \"assessmentTarget\": {\n" +
            "        \"@id\": \"$ORIGINAL_RESOURCE\"\n" +
            "      },\n" +
            "    \"completion\": {\n" +
            "        \"@value\": $RESULT_COMPLETION\n" +
            "     },\n" +
            "    \"log\": \"$RESULT_LOG\",\n" +
            "    \"outputFromTest\": {\n" +
            "      \"@id\": \"$TEST_ID\",\n" +
            "      \"@type\": \"Test\",\n" +
            "      \"description\": \"$TEST_DESCRIPTION\",\n" +
            "      \"identifier\": \"$TEST_ID\",\n" +
            "      \"title\": \"$TEST_TITLE\"\n" +
            "    }";
    public static String JSON_LD_TEST_CONTENT_FULL =
            JSON_LD_TEST_CONTENT_REDUCED+
            ",\n" +
            "    \"generatedAtTime\": {\n" +
            "        \"@type\": \"http://www.w3.org/2001/XMLSchema#date\",\n" +
            "        \"@value\": \"$RESULT_DATE\"\n" +
            "     },\n" +
            "    \"wasGeneratedBy\": {\n" +
            "       \"@type\": \"TestExecutionActivity\",\n" +
            "           \"used\": {\n" +
            "               \"@id\": \"$ORIGINAL_RESOURCE\"\n" +
            "           },\n" +
            "           \"wasAssociatedWith\": {\n" +
            "               \"@id\": \"$TEST_ID\",\n" +
            "               \"description\": \"$TEST_DESCRIPTION\",\n" +
            "               \"endpointDescription\": {\n" +
            "                   \"@id\": \"https://w3id.org/foops/api\"\n" +
            "               },\n" +
            "               \"endpointURL\": {\n" +
            "                   \"@id\": \"https://w3id.org/foops/api/assess/test/$TEST_ABBRV\"\n" +
            "               }\n" +
            "        }\n" +
            "      }\n";
    public static String JSON_LD_TEST_TEMPLATE_REDUCED =
            "{" +JSON_LD_TEST_CONTENT_REDUCED + "\n}";
    public static String JSON_LD_TEST_TEMPLATE_FULL =
            "{\n" + JSON_LD_TEST_CONTENT_FULL + "\n}";
    public static String JSON_LD_TEST_SET_TEMPLATE =
            "{\n" + FTR_CONTEXT +
            "    \"@id\": \"$RESULT_ID\",\n" +
            "    \"@type\": \"https://w3id.org/ftr#TestResultSet\",\n" +
            "    \"description\": \"$RESULT_DESCRIPTION\",\n" +
            "    \"identifier\": {\n" +
            "        \"@id\": \"$RESULT_ID\"\n" +
            "    },\n" +
            "    \"assessmentTarget\": {\n" +
            "        \"@id\": \"$ORIGINAL_RESOURCE\"\n" +
            "      },\n" +
            "      \"license\": {\n" +
            "        \"@id\": \"http://creativecommons.org/licenses/by/4.0/\"\n" +
            "      },\n" +
            "    \"title\": \"$RESULT_TITLE\",\n" +
            "    \"generatedAtTime\": {\n" +
            "        \"@type\": \"http://www.w3.org/2001/XMLSchema#date\",\n" +
            "        \"@value\": \"$RESULT_DATE\"\n" +
            "     },\n" +
            "    \"hadMember\": [" +
            "     $LIST_TEST_RESULTS" +
            "     ]" +
            "     }\n";
    public static final String FULL_LIST_OF_TESTS =
            "[" +
               "\""+FIND1_URL+"\","+
               "\""+URI1_URL+"\","+
               "\""+CN1_URL+"\","+
               "\""+DOC1_URL+"\","+
               "\""+RDF1_URL+"\","+
               "\""+OM1_URL+"\","+
               "\""+OM2_URL+"\","+
               "\""+OM3_URL+"\","+
               "\""+ OM4_1_URL +"\","+
               "\""+ OM4_2_URL +"\","+
               "\""+ OM5_1_URL +"\","+
               "\""+ OM5_2_URL +"\","+
               "\""+ FIND2_URL +"\","+
               "\""+ FIND3_URL +"\","+
               "\""+ FIND3_BIS_URL +"\","+
               "\""+ HTTP1_URL +"\","+
               "\""+ VOC1_URL +"\","+
               "\""+ VOC2_URL +"\","+
               "\""+ VOC3_URL +"\","+
               "\""+ VOC4_URL +"\","+
               "\""+ VER1_URL +"\","+
               "\""+ VER2_URL +"\","+
               "\""+ URI2_URL +"\""+
            "]";
}
