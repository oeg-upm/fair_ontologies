package entities.checks;

import entities.Check;
import entities.Ontology;
import fair.Constants;

public class Check_FIND3_BIS_MetadataAccessible extends Check {

    Check_FIND3_FindOntologyInRegistry find;

    public Check_FIND3_BIS_MetadataAccessible(Ontology o) {
        super(o);
    }

    /**
     * This check is essentially the same as FIND3, but in accessible.
     * @param o
     * @param find3
     */
    public Check_FIND3_BIS_MetadataAccessible(Ontology o, Check_FIND3_FindOntologyInRegistry find3) {
        super(o);
        this.find = find3;
        this.id = Constants.FIND3_BIS;
        this.description = Constants.FIND3_BIS_DESC;
        this.principle_id = "A2";
        this.category_id = Constants.ACCESSIBLE;
    }

    @Override
    public void check() {
        super.check();
        this.find.check();
        this.status = find.getStatus();
        this.explanation = find.getExplanation();
        this.total_passed_tests = find.getTotal_passed_tests();
    }
}
