/*
 * Copyright 2021-22 Ontology Engineering Group, Universidad Politecnica de Madrid, Spain
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Author: Daniel Garijo and Maria Poveda
 */

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
        this.title = Constants.URI1_TITLE;
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
                InputStream in = connection.getInputStream();
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
