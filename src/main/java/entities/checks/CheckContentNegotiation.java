package entities.checks;

import com.google.gson.annotations.Expose;
import entities.Check;
import entities.Ontology;
import fair.Constants;
import org.jsoup.nodes.Document;

public class CheckContentNegotiation extends Check {
    @Expose(serialize = false)
    private Document htmlDoc;


    public CheckContentNegotiation(Ontology o){
        super(o);
        this.htmlDoc = o.getHtmlDocumentation();
        this.id = Constants.CN1;
        this.category_id = Constants.ACCESSIBLE;
        this.principle_id ="A1";
        this.total_tests_run = 2;
    }

    @Override
    public void check() {
        super.check();
        String exp = "";
        if (htmlDoc != null){
            exp = "HTML, ";
            total_passed_tests += 1;
        }
        if (this.ontology != null){
            exp += "RDF";
            total_passed_tests += 1;
        }
        if (!"".equals(exp)){
            this.explanation = "Ontology available in: " +exp;
            this.status = Constants.OK;
        }else{
            this.explanation = "Ontology not available in RDF or HTML";
            this.status = Constants.ERROR;
        }
        this.score = total_passed_tests / total_tests_run;
    }

}
