package fair.Benchmarks;

import entities.Ontology;
import entities.checks.*;
import fair.Benchmark;
import fair.Constants;

import java.util.ArrayList;

public class CustomBenchmark extends Benchmark {

    /**
     * Class to generate a custom benchmark made from FOOPS! tests.
     * Typically only one test will be exposed, but this method allows for a potential combination on all tests
     * @param ontology ontology URI to load
     * @param ontologyOriginalUri identifier used to load the ontology in the app (needed for some tests)
     * @param testIDs tests that will be run
     */
    public CustomBenchmark(Ontology ontology,String ontologyOriginalUri, ArrayList<String> testIDs){
        ArrayList checks = new ArrayList<>();
        for (String item : testIDs) {
            switch (item){
                case Constants.CN1:
                    checks.add(new Check_CN1_ContentNegotiation(ontology));
                    break;
                case Constants.DOC1:
                    checks.add(new Check_DOC1_HTMLDoc(ontology));
                    break;
                case Constants.FIND1:
                    checks.add(new Check_FIND1_Prefix(ontology));
                    break;
                case Constants.FIND2:
                    checks.add(new Check_FIND2_PrefixInRegistry(ontology));
                    break;
                case Constants.FIND3:
                    checks.add(new Check_FIND3_FindOntologyInRegistry(ontology));
                    break;
                case Constants.FIND3_BIS:
                    Check_FIND3_FindOntologyInRegistry find3 = new Check_FIND3_FindOntologyInRegistry(ontology);
                    checks.add(find3);
                    checks.add(new Check_FIND3_BIS_MetadataAccessible(ontology,find3));
                    break;
                case Constants.HTTP1:
                    checks.add(new Check_HTTP1_AccessProtocol(ontology));
                    break;
                case Constants.OM1:
                    checks.add(new Check_OM1_MinimumMetadata(ontology));
                    break;
                case Constants.OM2:
                    checks.add(new Check_OM2_RecommendedMetadata(ontology));
                    break;
                case Constants.OM3:
                    checks.add(new Check_OM3_DetailedMetadata(ontology));
                    break;
                case Constants.OM4_1:
                    checks.add(new Check_OM4_1_License(ontology));
                    break;
                case Constants.OM4_2:
                    checks.add(new Check_OM4_2_LicenseIsResolvable(ontology));
                    break;
                case Constants.OM5_1:
                    checks.add(new Check_OM5_1_ProvenanceMetadataBasic(ontology));
                    break;
                case Constants.OM5_2:
                    checks.add(new Check_OM5_2_ProvenanceMetadataFull(ontology));
                    break;
                case Constants.PURL1:
                    checks.add(new Check_PURL1_PersistentURIs(ontology));
                    break;
                case Constants.URI1:
                    checks.add(new Check_URI1_URIResolvable(ontology));
                    break;
                case Constants.URI2:
                    checks.add(new Check_URI2_OntologyURIEqualToID(ontology, ontologyOriginalUri));
                    break;
                case Constants.VER1:
                    checks.add(new Check_VER1_VersionIRI(ontology));
                    break;
                case Constants.VER2:
                    checks.add(new Check_VER2_ResolvableVersionIRI(ontology));
                    break;
                case Constants.VOC1:
                    checks.add(new Check_VOC1_VocabReuseMetadata(ontology));
                    break;
                case Constants.VOC2:
                    checks.add(new Check_VOC2_VocabReuse(ontology));
                    break;
                case Constants.VOC3:
                    checks.add(new Check_VOC3_TermMetadataLabel(ontology));
                    break;
                case Constants.VOC4:
                    checks.add(new Check_VOC4_TermMetadataDescription(ontology));
                    break;
                case Constants.RDF1:
                    checks.add(new Check_RDF1_RDFAvailability(ontology));
                    break;
            }
        }
        this.setChecks(checks);
    }
}
