package entities.checks;

import entities.Check;
import entities.Ontology;

/**
 * Given an ontology, this checks whether an RDF representation is available
 */

public class CheckRDF extends Check {
    public CheckRDF(Ontology o) {
        super(o);
    }
}
