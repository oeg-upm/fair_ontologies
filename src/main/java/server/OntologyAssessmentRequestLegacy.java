package server;

// import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;


public class OntologyAssessmentRequestLegacy {
    @Schema(
            description = "Ontology URI to assess",
            example = "https://w3id.org/example#",
            required = true
    )
    private String ontologyUri;

    // Getter and Setter
    public String getOntologyUri() {
        return ontologyUri;
    }

    public void setOntologyUri(String ontologyUri) {
        this.ontologyUri = ontologyUri;
    }
}
