package entities;

/**
 * Class representing a JSON response accepted by FOOPS.
 * We accept from URI or from file
 */
public class Response {
    private String ontologyUri;

    public String getOntologyUri() {
        return ontologyUri;
    }

    public void setOntologyUri(String ontologyUri) {
        this.ontologyUri = ontologyUri;
    }
}


