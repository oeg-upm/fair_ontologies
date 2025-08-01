package fair.Benchmarks;

import entities.Ontology;
import entities.checks.*;
import fair.Benchmark;
import fair.Constants;

import java.util.ArrayList;

public class FileBenchmark extends Benchmark {

    /**
     * Benchmark that includes tests for files and ontology code. No accessibility tests are performed, since
     * this is meant for aiding researchers that have not made their ontologies available online yet.
     * Benchmark URI: https://w3id.org/foops/benchmark/PRE
     * @param ontology ontology object loaded in memory
     */
    public FileBenchmark(Ontology ontology){
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
        Check_VOC1_VocabReuseMetadata voc1 = new Check_VOC1_VocabReuseMetadata(ontology);
        Check_VOC2_VocabReuse voc2 = new Check_VOC2_VocabReuse(ontology);
        Check_VOC3_TermMetadataLabel voc3 = new Check_VOC3_TermMetadataLabel(ontology);
        Check_VOC4_TermMetadataDescription voc4 = new Check_VOC4_TermMetadataDescription(ontology);
        Check_VER1_VersionIRI ver1 = new Check_VER1_VersionIRI(ontology);
        ArrayList checks = new ArrayList<>();
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
        checks.add(voc1);
        checks.add(voc2);
        checks.add(voc3);
        checks.add(voc4);
        checks.add(ver1);
        this.setChecks(checks);
        this.setName(Constants.BENCHMARK_PRE_NAME);
        this.setDescription(Constants.BENCHMARK_PRE_DESCRIPTION);
    }
}
