package entities.checks;

import entities.Check;
import entities.Ontology;

/**
 * Given an ontology, this check will verify whether all terms are correctly annotated with label and description
 * (reusability)
 *
 * TO DO
 */

public class CheckTermMetadata extends Check {
    public CheckTermMetadata(Ontology o) {
        super(o);
    }

}
