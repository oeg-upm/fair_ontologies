package entities.checks;

import entities.Check;
import entities.Ontology;

/**
 * Very simple check: if the ontology uses a URI, it is successful.
 * We expect most ontologies to pass this test.
 */
public class CheckURI extends Check {
    public CheckURI(Ontology o) {
        super(o);
    }
}
