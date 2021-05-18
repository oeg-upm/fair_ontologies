package entities.checks;

import entities.Check;
import entities.Ontology;

/**
 * This check verifies if the ontology uses version IRI and if that IRI is resolvable
 */
public class CheckResolvableURIVersion extends Check {
    public CheckResolvableURIVersion(Ontology o) {
        super(o);
    }
}
