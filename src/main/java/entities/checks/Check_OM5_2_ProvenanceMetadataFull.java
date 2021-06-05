package entities.checks;

import entities.Check;
import entities.Ontology;
import fair.Constants;

public class Check_OM5_2_ProvenanceMetadataFull extends Check {
    public Check_OM5_2_ProvenanceMetadataFull(Ontology o){
        super(o);
        this.category_id = Constants.REUSABLE;
        this.id = Constants.OM5_2;
        this.description = Constants.OM5_2_DESC;
        this.principle_id = "R1.2";
        this.total_tests_run = Constants.PROVENANCE_METADATA_DETAILED.length;
    }

    @Override
    public void check() {
        super.check();
        String exp = "";
        for (String m : Constants.PROVENANCE_METADATA_DETAILED) {
            if (!this.ontology.getSupportedMetadata().contains(m)) {
                exp += m + ", ";
            } else {
                total_passed_tests += 1;
            }
        }
        String optional = "";
        for (String m : Constants.PROVENANCE_METADATA_OPTIONAL) {
            if (!this.ontology.getSupportedMetadata().contains(m)) {
                optional += m + ", ";
            }
        }
        //remove last comma
        if ("".equals(exp)) {
            explanation = "All detailed provenance metadata found!";
            this.status = Constants.OK;
        } else {
            this.status = Constants.ERROR;
            explanation = Constants.OM5_2_EXPLANATION + exp.substring(0, exp.length() - 2);
        }
        if (!"".equals(optional)){
            explanation += "\nWarning: We could not find the following provenance metadata: "+
                    optional.substring(0,optional.length() -2) + " Please consider adding them (if appropriate).";
        }

    }
}
