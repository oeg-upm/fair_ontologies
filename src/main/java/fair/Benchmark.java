package fair;

import entities.Check;
import entities.Ontology;
import entities.checks.*;

import java.util.List;
import java.util.ArrayList;


public class Benchmark {
    private ArrayList<Check> checks;
    private String name;
    private String description;

    public ArrayList<Check> getChecks() {
        return checks;
    }

    public void setChecks(ArrayList<Check> checks) {
        this.checks = checks;
    }

    public String getDescription() {
        return description;
    }

    public String getName(){
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
