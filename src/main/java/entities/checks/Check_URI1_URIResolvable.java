package entities.checks;

import entities.Check;
import entities.Ontology;
import fair.Constants;
import fair.Utils;

import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Why this test? You could have loaded an ontology through a file, which is not resolvable.
 * Also, you may have loaded an ontology through a URI, which is different from the onto URI used in the
 * document
 */
public class Check_URI1_URIResolvable extends Check {
    public Check_URI1_URIResolvable(Ontology o){
        super(o);
        this.id = Constants.URI1;
        this.category_id = Constants.FINDABLE;
        this.principle_id ="F1";
        this.description = Constants.URI1_DESC;
    }

    @Override
    public void check() {
        super.check();
        // need to check (again) if the ontology resolves, but this time we do not download it
        for (String serialization : Constants.POSSIBLE_VOCAB_SERIALIZATIONS) {
            try {
                HttpURLConnection connection = Utils.doNegotiation(this.ontology_URI,serialization);
                InputStream in = (InputStream) connection.getInputStream();
                in.close();
                explanation = Constants.URI1_EXPLANATION_OK + " in "+serialization;
                status = Constants.OK;
                total_passed_tests ++;
                return;
            } catch (Exception e) {
                //continue
            }

        }
        status = Constants.ERROR;
        explanation = Constants.URI1_EXPLANATION_ERROR;
    }
}
