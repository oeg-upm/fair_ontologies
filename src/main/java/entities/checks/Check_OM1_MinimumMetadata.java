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

/**
 * Thi Class checks if minimum metadata is present:
 * title, description, license, versioniri, creator, creationDate, nsURI.
 */

public class Check_OM1_MinimumMetadata extends Check {

    public Check_OM1_MinimumMetadata(Ontology o) {
        super(o);
        this.category_id = Constants.FINDABLE;
        this.id = Constants.OM1;
        this.title = Constants.OM1_TITLE;
        this.description = Constants.OM1_DESC;
        this.principle_id = "F2";
        this.total_tests_run = Constants.MINIMUM_METADATA.length;
    }

    @Override
    public void check() {
        super.check();
        String exp = "";
        for (String m:Constants.MINIMUM_METADATA){
            if(!this.ontology.getSupportedMetadata().contains(m)){
                exp += m+", ";
            }else{
                total_passed_tests += 1;
            }
        }
        //remove last comma
        if("".equals(exp)){
            explanation = Constants.OM1_EXPLANATION_OK;
            this.status = Constants.OK;
        }else {
            this.status = Constants.ERROR;
            explanation = Constants.OM1_EXPLANATION_ERROR + exp.substring(0, exp.length() - 2);
        }

    }
}
