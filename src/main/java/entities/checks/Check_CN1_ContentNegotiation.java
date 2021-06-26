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

public class Check_CN1_ContentNegotiation extends Check {


    public Check_CN1_ContentNegotiation(Ontology o){
        super(o);
        this.id = Constants.CN1;
        this.title = Constants.CN1_TITLE;
        this.category_id = Constants.ACCESSIBLE;
        this.principle_id ="A1";
        this.total_tests_run = 2;
        this.description = Constants.CN1_DESC;
    }


    @Override
    public void check() {
        super.check();
        String exp = "";
        if (this.ontology.getHtmlDocumentation() != null){
            exp = "HTML, ";
            total_passed_tests += 1;
        }
        if (this.ontology.getOntologyModel() != null){
            exp += "RDF";
            total_passed_tests += 1;
        }
        if (!"".equals(exp)){
            this.explanation = Constants.CN1_DESC_EXPLANATION_OK +exp;
            this.status = Constants.OK;
        }else{
            this.explanation = Constants.CN1_DESC_EXPLANATION_ERROR;
            this.status = Constants.ERROR;
        }
    }

}
