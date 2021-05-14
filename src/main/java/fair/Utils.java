package fair;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * Method that will download the ontology to document with Widoco.
     *
     * @param isFromFile boolean to indicate whether the ontology is from file or from URI.
     * @throws java.lang.Exception
     */
    public static OWLOntology loadModelToDocument(String pathOrURI,boolean isFromFile) throws Exception {
        String ontologyPath = pathOrURI;
        if (!isFromFile) {
            ontologyPath = "Ontology";
            downloadOntology(pathOrURI, ontologyPath);
        }
        logger.info("Loading ontology ");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntologyLoaderConfiguration loadingConfig = new OWLOntologyLoaderConfiguration();
        loadingConfig = loadingConfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
        return manager.loadOntologyFromOntologyDocument(new FileDocumentSource(new File(ontologyPath)), loadingConfig);
    }

    /**
     * Method that will download an ontology given its URI, doing content
     * negotiation The ontology will be downloaded in the first serialization
     * available (see Constants.POSSIBLE_VOCAB_SERIALIZATIONS)
     *
     * @param uri the URI of the ontology
     * @param downloadPath path where the ontology will be saved locally.
     */
    public static void downloadOntology(String uri, String downloadPath) {

        for (String serialization : Constants.POSSIBLE_VOCAB_SERIALIZATIONS) {
            logger.info("Attempting to download vocabulary in " + serialization);
            try {
                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setRequestProperty("Accept", serialization);
                int status = connection.getResponseCode();
                boolean redirect = false;
                if (status != HttpURLConnection.HTTP_OK) {
                    if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
                            || status == HttpURLConnection.HTTP_SEE_OTHER)
                        redirect = true;
                }
                // there are some vocabularies with multiple redirections:
                // 301 -> 303 -> owl
                while (redirect) {
                    String newUrl = connection.getHeaderField("Location");
                    connection = (HttpURLConnection) new URL(newUrl).openConnection();
                    connection.setRequestProperty("Accept", serialization);
                    status = connection.getResponseCode();
                    if (status != HttpURLConnection.HTTP_MOVED_TEMP && status != HttpURLConnection.HTTP_MOVED_PERM
                            && status != HttpURLConnection.HTTP_SEE_OTHER)
                        redirect = false;
                }
                InputStream in = (InputStream) connection.getInputStream();
                Files.copy(in, Paths.get(downloadPath), StandardCopyOption.REPLACE_EXISTING);
                in.close();
                break; // if the vocabulary is downloaded, then we don't download it for the other
                // serializations
            } catch (Exception e) {
                final String message = "Failed to download vocabulary in RDF format [" + serialization +"]: ";
                logger.error(message + e.toString());
                throw new RuntimeException(message, e);
            }
        }
    }

    /**
     * Writes a model into a file
     *
     * @param m the manager
     * @param o the ontology to write
     * @param f the format in which should be written
     * @param outPath
     */
    public static void writeModel(OWLOntologyManager m, OWLOntology o, OWLDocumentFormat f, String outPath) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(outPath);
            m.saveOntology(o, f, out);
            out.close();
        } catch (Exception ex) {
            logger.error("Error while writing the model to file " + ex.getMessage());
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
