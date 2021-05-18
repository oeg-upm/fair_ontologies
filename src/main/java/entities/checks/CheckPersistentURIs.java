package entities.checks;

import entities.Check;
import entities.Ontology;
import fair.Constants;

public class CheckPersistentURIs extends Check {
    public CheckPersistentURIs(Ontology o){
        super(o);
        this.category_id = Constants.FINDABLE;
        this.principle_id = "F1";
        this.id = Constants.PURL;
        this.total_tests_run = 1;
    }

    @Override
    public void check() {
        super.check();
        //Note: test could be enhanced so it checks for http[s] + any of the URLs below
        if (this.ontology_URI.contains("w3id.org") ||
                this.ontology_URI.contains("doi.org") ||
                this.ontology_URI.contains("purl.org") ||
                this.ontology_URI.contains("www.w3.org")){
            this.status = Constants.OK;
            this.explanation = "URI is w3id, purl or a W3C URL";
            this.total_passed_tests +=1;
        }else{
            this.status = Constants.ERROR;
            this.explanation = "URI is not using a persistent id (purl, w3id, etc.)";
        }
    }

}
