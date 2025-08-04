package server;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class OntologyAssessmentRequest {
    @ApiModelProperty(
            value = "Ontology URI to assess",
            example = "https://w3id.org/example#",
            required = true
    )
    //Named because of specification conventions
    @JsonProperty("resource_identifier") // makes Swagger ignore this if used improperly — workaround below
    private String resourceIdentifier;

    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(String resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }
    }

