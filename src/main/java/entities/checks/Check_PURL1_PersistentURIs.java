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

public class Check_PURL1_PersistentURIs extends Check {
    public Check_PURL1_PersistentURIs(Ontology o){
        super(o);
        this.category_id = Constants.FINDABLE;
        this.principle_id = "F1";
        this.id = Constants.PURL1_URL;
        this.title = Constants.PURL1_TITLE;
        this.description = Constants.PURL1_DESC;
        this.abbreviation = Constants.PURL1;
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
            this.explanation = Constants.PURL1_EXPLANATION_OK;
            this.total_passed_tests +=1;
        }else{
            this.status = Constants.ERROR;
            this.explanation = Constants.PURL1_EXPLANATION_ERROR;
        }
    }

}
