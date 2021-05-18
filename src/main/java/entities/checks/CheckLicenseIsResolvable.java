package entities.checks;

import entities.Check;
import entities.Ontology;

/**
 * Given an ontology, this check verifies if its license is resolvable.
 */

public class CheckLicenseIsResolvable extends Check {

    public CheckLicenseIsResolvable(Ontology o) {
        super(o);
    }
}
