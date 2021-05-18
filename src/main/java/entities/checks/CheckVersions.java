package entities.checks;

import entities.Check;
import entities.Ontology;

public class CheckVersions extends Check {
    public CheckVersions(Ontology o) {
        super(o);
    }
    /**
     * Check if the ontology has a proper version scheme.
     */
}
