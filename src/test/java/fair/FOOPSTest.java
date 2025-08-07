package fair;

import entities.Ontology;
import entities.checks.*;
import org.junit.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.FOOPSController;
import server.FileTooLargeException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class FOOPSTest {
    Logger logger = LoggerFactory.getLogger(FOOPSController.class);

    /**
     * This test verifies that the 61 concepts in the conceptual_mapping vocabulary are documented.
     * The test uses skos for descriptions.
     * One of the labels uses obo:0000118 (alt label)
     */
    @Test
    public void skosAndRdfsAnnotationsUsedForOntology(){
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("conceptual_mapping.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            assertEquals (61,f.getOntology().getTermsWithDescription().size());
            assertEquals (61,f.getOntology().getTermsWithLabel().size());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     * This test verifies that an ontology with a blank node as an author works
     */
    @Test
    public void blankNodesInAnnotations(){
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("test_blank_node.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            // there must be one author
            assertEquals (1,f.getOntology().getAuthors().size());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     * This test verifies if an ontology with uri purl.something.org passes as persistent
     */
    @Test
    public void persistentURI(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("test_persistent_uri.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            Check_PURL1_PersistentURIs check = new Check_PURL1_PersistentURIs(f.getOntology());
            check.check();
            // URI should be persistent
            assertEquals(Constants.OK, check.getStatus());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     * This test verifies if the access rights of an ontology are properly detected
     */
    @Test
    public void accessRightsArePresent(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("test_rights.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            Check_OM4_1_License check = new Check_OM4_1_License(f.getOntology());
            check.check();
            // URI should be persistent
            assertEquals(Constants.OK, check.getStatus());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file for test_rights.ttl");
            fail();
        }
    }

    /**
     * This test verifies that an ontology with all metadata passes the metadata tests
     */
    @Test
    public void annotatedOntology(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("ontology_100.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            Check_OM1_MinimumMetadata ch1 = new Check_OM1_MinimumMetadata(f.getOntology());
            ch1.check();
            Check_OM2_RecommendedMetadata ch2 = new Check_OM2_RecommendedMetadata(f.getOntology());
            ch2.check();
            Check_OM3_DetailedMetadata ch3 = new Check_OM3_DetailedMetadata(f.getOntology());
            ch3.check();
            // all metadata is in the ontology
            assertEquals(Constants.OK, ch1.getStatus());
            assertEquals(Constants.OK, ch2.getStatus());
            assertEquals(Constants.OK, ch3.getStatus());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     * This test verifies that an ontology passes the availability in registry through an annotation
     * (schema:includedInDataCatalog)
     */
    @Test
    public void ontologyInDataCatalog(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("ontology_included_in_catalog.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            Check_FIND3_FindOntologyInRegistry ch1 = new Check_FIND3_FindOntologyInRegistry(f.getOntology());
            ch1.check();
            // all metadata is in the ontology
            assertEquals(Constants.OK, ch1.getStatus());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     * This test verifies that an ontology with a link to a pdf and access right correctly defines a license
     * Even if the license does not resolve
     */
    @Test
    public void ontologyWithStrangeLicense(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("weird_license.owl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            Check_OM4_1_License ch1 = new Check_OM4_1_License(f.getOntology());
            Check_OM4_2_LicenseIsResolvable ch2 = new Check_OM4_2_LicenseIsResolvable(f.getOntology());
            ch1.check();
            ch2.check();
            // all metadata is in the ontology
            assertEquals(Constants.OK, ch1.getStatus());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     * This test verifies that if an ontology extends metadata properties, they still get accepted.
     */
    @Test
    public void testExtendedProperties(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("test_extended_annotation_properties.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            Check_OM1_MinimumMetadata ch1 = new Check_OM1_MinimumMetadata(f.getOntology());
            ch1.check();
            // all metadata is in the ontology
            assertEquals(Constants.OK, ch1.getStatus());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     * This test tries to load an ontology with errors (should fail)
     */
    @Test
    public void testOntologyWithErrors(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("test_onto_with_errors.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            assertEquals(null,f.getOntology().getOntologyModel());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     * This test tries to load a skos vocabulary
     */
    @Test
    public void testSKOS(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File is = new File(classLoader.getResource("skos_example.ttl").getFile());
            FOOPS f = new FOOPS(is.toString(), true);
            Check_VOC3_TermMetadataLabel l = new Check_VOC3_TermMetadataLabel(f.getOntology());
            l.check();
            assertEquals(Constants.OK,l.getStatus());
            f.removeTemporaryFolders();
        } catch (Exception e) {
            logger.error("Could not load the resource file");
            fail();
        }
    }

    /**
     *  Local Test to verify exceptions when files are too big.
     */
    @Test
    public void testFileTooBig(){
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("test");
            File testFile = tempDir.resolve("large_test_file.bin").toFile();

            // Create a file slightly larger than 50 MB (51 MB)
            try (FileOutputStream fos = new FileOutputStream(testFile)) {
                byte[] buffer = new byte[1024 * 1024]; // 1 MB buffer
                for (int i = 0; i < 51; i++) {
                    fos.write(buffer);
                }
            }
            assertThrows(FileTooLargeException.class, () -> {
                new FOOPS(testFile.getAbsolutePath(), true);
            });
        }catch(Exception e){
            throw new RuntimeException("Test failed due to IOException: " + e.getMessage(), e);
        }finally {
            if (tempDir != null && Files.exists(tempDir)) {
                try {
                    Files.delete(tempDir);
                } catch (IOException ignored) {
                    // Swallow exception if tempDir isn't empty
                    tempDir.toFile().deleteOnExit();
                }
            }
        }
    }
}
