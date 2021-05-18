package entities.checks;

import entities.Check;
import entities.Ontology;

/**
 * Given an ontology, this check inspects its namespaces to verify other vocabs are extended/used.
 * Note that some will have to be ignored (e.g., the metadata ones)
 */
public class CheckNamespaces extends Check {
    public CheckNamespaces(Ontology o) {
        super(o);
    }
}
