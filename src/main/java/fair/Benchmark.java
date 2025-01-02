package fair;

import entities.Check;
import entities.Ontology;
import entities.checks.*;

import java.util.List;
import java.util.ArrayList;


public class Benchmark {
    private ArrayList<Check> checks;
    public ArrayList<Check> uriChecks (Ontology ontology, String o ){
        Check_PURL1_PersistentURIs f1 = new Check_PURL1_PersistentURIs(ontology);
        Check_URI1_URIResolvable uri1 = new Check_URI1_URIResolvable(ontology);
        Check_CN1_ContentNegotiation a1 = new Check_CN1_ContentNegotiation(ontology);
        Check_DOC1_HTMLDoc d1 = new Check_DOC1_HTMLDoc(ontology);
        Check_RDF1_RDFAvailability rdf1 = new Check_RDF1_RDFAvailability(ontology);
        Check_OM1_MinimumMetadata om1 = new Check_OM1_MinimumMetadata(ontology);
        Check_OM2_RecommendedMetadata om2 = new Check_OM2_RecommendedMetadata(ontology);
        Check_OM3_DetailedMetadata om3 = new Check_OM3_DetailedMetadata(ontology);
        Check_OM4_1_License om41 = new Check_OM4_1_License(ontology);
        Check_OM4_2_LicenseIsResolvable om42 = new Check_OM4_2_LicenseIsResolvable(ontology);
        Check_OM5_1_ProvenanceMetadataBasic om51 = new Check_OM5_1_ProvenanceMetadataBasic(ontology);
        Check_OM5_2_ProvenanceMetadataFull om52 = new Check_OM5_2_ProvenanceMetadataFull(ontology);
        Check_FIND1_Prefix find1 = new Check_FIND1_Prefix(ontology);
        Check_FIND2_PrefixInRegistry find2 = new Check_FIND2_PrefixInRegistry(ontology);
        Check_FIND3_FindOntologyInRegistry find3 = new Check_FIND3_FindOntologyInRegistry(ontology);
        Check_FIND3_BIS_MetadataAccessible find3_bis = new Check_FIND3_BIS_MetadataAccessible(ontology,find3);
        Check_HTTP1_AccessProtocol http1 = new Check_HTTP1_AccessProtocol(ontology);
        Check_VOC1_VocabReuseMetadata voc1 = new Check_VOC1_VocabReuseMetadata(ontology);
        Check_VOC2_VocabReuse voc2 = new Check_VOC2_VocabReuse(ontology);
        Check_VOC3_TermMetadataLabel voc3 = new Check_VOC3_TermMetadataLabel(ontology);
        Check_VOC4_TermMetadataDescription voc4 = new Check_VOC4_TermMetadataDescription(ontology);
        Check_VER1_VersionIRI ver1 = new Check_VER1_VersionIRI(ontology);
        Check_VER2_ResolvableVersionIRI ver2 = new Check_VER2_ResolvableVersionIRI(ontology);
        Check_URI2_OntologyURIEqualToID uri2 = new Check_URI2_OntologyURIEqualToID(ontology, o);
        checks = new ArrayList<>();
        checks.add(f1);
        checks.add(uri1);
        checks.add(a1);
        checks.add(d1);
        checks.add(rdf1);
        checks.add(om1);
        checks.add(om2);
        checks.add(om3);
        checks.add(om41);
        checks.add(om42);
        checks.add(om51);
        checks.add(om52);
        checks.add(find1);
        checks.add(find2);
        checks.add(find3);
        checks.add(find3_bis);
        checks.add(http1);
        checks.add(voc1);
        checks.add(voc2);
        checks.add(voc3);
        checks.add(voc4);
        checks.add(ver1);
        checks.add(ver2);
        checks.add(uri2);

        return checks;
    }

    public ArrayList<Check> fileChecks (Ontology ontology){
        Check_PURL1_PersistentURIs f1 = new Check_PURL1_PersistentURIs(ontology);

        Check_RDF1_RDFAvailability rdf1 = new Check_RDF1_RDFAvailability(ontology);
        Check_OM1_MinimumMetadata om1 = new Check_OM1_MinimumMetadata(ontology);
        Check_OM2_RecommendedMetadata om2 = new Check_OM2_RecommendedMetadata(ontology);
        Check_OM3_DetailedMetadata om3 = new Check_OM3_DetailedMetadata(ontology);
        Check_OM4_1_License om41 = new Check_OM4_1_License(ontology);
        Check_OM4_2_LicenseIsResolvable om42 = new Check_OM4_2_LicenseIsResolvable(ontology);
        Check_OM5_1_ProvenanceMetadataBasic om51 = new Check_OM5_1_ProvenanceMetadataBasic(ontology);
        Check_OM5_2_ProvenanceMetadataFull om52 = new Check_OM5_2_ProvenanceMetadataFull(ontology);
        Check_FIND1_Prefix find1 = new Check_FIND1_Prefix(ontology);

        Check_HTTP1_AccessProtocol http1 = new Check_HTTP1_AccessProtocol(ontology);

        Check_VOC1_VocabReuseMetadata voc1 = new Check_VOC1_VocabReuseMetadata(ontology);
        Check_VOC2_VocabReuse voc2 = new Check_VOC2_VocabReuse(ontology);
        Check_VOC3_TermMetadataLabel voc3 = new Check_VOC3_TermMetadataLabel(ontology);
        Check_VOC4_TermMetadataDescription voc4 = new Check_VOC4_TermMetadataDescription(ontology);
        Check_VER1_VersionIRI ver1 = new Check_VER1_VersionIRI(ontology);

        checks = new ArrayList<>();
        checks.add(f1);

        checks.add(rdf1);
        checks.add(om1);
        checks.add(om2);
        checks.add(om3);
        checks.add(om41);
        checks.add(om42);
        checks.add(om51);
        checks.add(om52);
        checks.add(find1);

        //checks.add(http1); //ESTE TEST LE HE DEJADO PQ HAY Q HACER MINIMO 1 DE CADA TIPO
        //AÑADIR CASO 0

        checks.add(voc1);
        checks.add(voc2);
        checks.add(voc3);
        checks.add(voc4);
        checks.add(ver1);


        return checks;
    }

    public List<Check> customChecks (Ontology ontology, String[] customChecks, String o){
        checks = new ArrayList<>();

        for(int i = 0; i < customChecks.length; i++){
            if(customChecks[i] == "f1"){
                Check_PURL1_PersistentURIs f1 = new Check_PURL1_PersistentURIs(ontology);
                checks.add(f1);
            }
            if(customChecks[i] == "uri1"){
                Check_URI1_URIResolvable uri1 = new Check_URI1_URIResolvable(ontology);
                checks.add(uri1);
            }
            if(customChecks[i] == "a1"){
                Check_CN1_ContentNegotiation a1 = new Check_CN1_ContentNegotiation(ontology);
                checks.add(a1);
            }
            if(customChecks[i] == "d1"){
                Check_DOC1_HTMLDoc d1 = new Check_DOC1_HTMLDoc(ontology);
                checks.add(d1);
            }
            if(customChecks[i] == "rdf1"){
                Check_RDF1_RDFAvailability rdf1 = new Check_RDF1_RDFAvailability(ontology);
                checks.add(rdf1);
            }
            if(customChecks[i] == "om1"){
                Check_OM1_MinimumMetadata om1 = new Check_OM1_MinimumMetadata(ontology);
                checks.add(om1);
            }
            if(customChecks[i] == "om2"){
                Check_OM2_RecommendedMetadata om2 = new Check_OM2_RecommendedMetadata(ontology);
                checks.add(om2);
            }
            if(customChecks[i] == "om3"){
                Check_OM3_DetailedMetadata om3 = new Check_OM3_DetailedMetadata(ontology);
                checks.add(om3);
            }
            if(customChecks[i] == "om41"){
                Check_OM4_1_License om41 = new Check_OM4_1_License(ontology);
                checks.add(om41);
            }
            if(customChecks[i] == "om42"){
                Check_OM4_2_LicenseIsResolvable om42 = new Check_OM4_2_LicenseIsResolvable(ontology);
                checks.add(om42);
            }
            if(customChecks[i] == "om51"){
                Check_OM5_1_ProvenanceMetadataBasic om51 = new Check_OM5_1_ProvenanceMetadataBasic(ontology);
                checks.add(om51);
            }
            if(customChecks[i] == "om52"){
                Check_OM5_2_ProvenanceMetadataFull om52 = new Check_OM5_2_ProvenanceMetadataFull(ontology);
                checks.add(om52);
            }
            if(customChecks[i] == "find1"){
                Check_FIND1_Prefix find1 = new Check_FIND1_Prefix(ontology);
                checks.add(find1);
            }
            if(customChecks[i] == "find2"){
                Check_FIND2_PrefixInRegistry find2 = new Check_FIND2_PrefixInRegistry(ontology);
                checks.add(find2);
            }
            if(customChecks[i] == "find3"){
                Check_FIND3_FindOntologyInRegistry find3 = new Check_FIND3_FindOntologyInRegistry(ontology);
                checks.add(find3);
            }
            if(customChecks[i] == "find3_bis"){
                Check_FIND3_FindOntologyInRegistry find3 = new Check_FIND3_FindOntologyInRegistry(ontology);
                Check_FIND3_BIS_MetadataAccessible find3_bis = new Check_FIND3_BIS_MetadataAccessible(ontology,find3);
                checks.add(find3_bis);
            }
            if(customChecks[i] == "http1"){
                Check_HTTP1_AccessProtocol http1 = new Check_HTTP1_AccessProtocol(ontology);
                checks.add(http1);
            }
            if(customChecks[i] == "voc1"){
                Check_VOC1_VocabReuseMetadata voc1 = new Check_VOC1_VocabReuseMetadata(ontology);
                checks.add(voc1);
            }
            if(customChecks[i] == "voc2"){
                Check_VOC2_VocabReuse voc2 = new Check_VOC2_VocabReuse(ontology);
                checks.add(voc2);
            }
            if(customChecks[i] == "voc3"){
                Check_VOC3_TermMetadataLabel voc3 = new Check_VOC3_TermMetadataLabel(ontology);
                checks.add(voc3);
            }
            if(customChecks[i] == "voc4"){
                Check_VOC4_TermMetadataDescription voc4 = new Check_VOC4_TermMetadataDescription(ontology);
                checks.add(voc4);
            }
            if(customChecks[i] == "ver1"){
                Check_VER1_VersionIRI ver1 = new Check_VER1_VersionIRI(ontology);

                checks.add(ver1);
            }
            if(customChecks[i] == "ver2"){
                Check_VER2_ResolvableVersionIRI ver2 = new Check_VER2_ResolvableVersionIRI(ontology);
                checks.add(ver2);
            }
            if(customChecks[i] == "uri2"){
                Check_URI2_OntologyURIEqualToID uri2 = new Check_URI2_OntologyURIEqualToID(ontology, o);
                checks.add(uri2);
            }
        }
        return checks;
    }
}
