package fair;

import entities.Check;
import entities.Ontology;
import entities.checks.*;

import java.util.List;
import java.util.ArrayList;


public class Benchmark {
    private ArrayList<Check> checks;

    public ArrayList<Check> getChecks() {
        return checks;
    }

    public void setChecks(ArrayList<Check> checks) {
        this.checks = checks;
    }
}
