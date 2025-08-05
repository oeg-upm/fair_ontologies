package fair;

import entities.Ontology;
import entities.checks.Check_PURL1_PersistentURIs;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.*;

public class FOOPSTest {

    /**
     * This test verifies that the 61 concepts in the conceptual_mapping vocabulary are documented.
     * The test uses skos for descriptions.
     * One of the labels uses obo:0000118 (alt label)
     */
    @Test
    public void skosAndRdfsAnnotationsUsedForOntology(){
        ClassLoader classLoader = getClass().getClassLoader();
        File is = new File(classLoader.getResource("conceptual_mapping.ttl").getFile());
        FOOPS f = new FOOPS(is.toString(), true);
        assertEquals (61,f.getOntology().getTermsWithDescription().size());
        assertEquals (61,f.getOntology().getTermsWithLabel().size());
        f.removeTemporaryFolders();
    }

    /**
     * This test verifies that an ontology with a blank node as an author works
     */
    @Test
    public void blankNodesInAnnotations(){
        ClassLoader classLoader = getClass().getClassLoader();
        File is = new File(classLoader.getResource("test_blank_node.ttl").getFile());
        FOOPS f = new FOOPS(is.toString(), true);
        // there must be one author
        assertEquals (1,f.getOntology().getAuthors().size());
        f.removeTemporaryFolders();
    }

    /**
     * This test verifies if an ontology with uri purl.something.org passes as persistent
     */
    @Test
    public void persistentURI(){
        ClassLoader classLoader = getClass().getClassLoader();
        File is = new File(classLoader.getResource("test_persistent_uri.ttl").getFile());
        FOOPS f = new FOOPS(is.toString(), true);
        Check_PURL1_PersistentURIs check = new Check_PURL1_PersistentURIs(f.getOntology());
        check.check();
        // URI should be persistent
        assertEquals(Constants.OK, check.getStatus());
        f.removeTemporaryFolders();

    }
}
