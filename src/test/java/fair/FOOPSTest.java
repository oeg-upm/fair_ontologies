package fair;

import entities.Ontology;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.*;

public class FOOPSTest {

    /**
     * This test verifies that the 61 concepts in the conceptual_mapping vocabulary are documented.
     * The test uses skos for an owl ontology
     */
    @Test
    public void skosAndRdfsAnnotationsUsedForOntology(){
        ClassLoader classLoader = getClass().getClassLoader();
        File is = new File(classLoader.getResource("conceptual_mapping.ttl").getFile());
        FOOPS f = new FOOPS(is.toString(), true);
        assertEquals (61,f.getOntology().getTermsWithDescription().size());
        f.removeTemporaryFolders();
    }
}
