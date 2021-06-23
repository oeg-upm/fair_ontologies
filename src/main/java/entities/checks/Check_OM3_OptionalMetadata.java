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

public class Check_OM3_OptionalMetadata extends Check {
    public Check_OM3_OptionalMetadata(Ontology o) {
        super(o);
        this.category_id = Constants.REUSABLE;
        this.id = Constants.OM3;
        this.description = Constants.OM3_DESC;
        this.principle_id = "R1";
        this.total_tests_run = Constants.OPTIONAL_METADATA.length;
    }
    /**
     * This check verifies whether the detected metadata is the optional one
     */

    @Override
    public void check() {
        super.check();
        String exp = "";
        for (String m: Constants.OPTIONAL_METADATA){
            if(!this.ontology.getSupportedMetadata().contains(m)){
                exp += m+", ";
            }else{
                total_passed_tests += 1;
            }
        }
        //remove last comma
        if("".equals(exp)){
            explanation = "All optional metadata found!";
            this.status = Constants.OK;
        }else {
            this.status = Constants.ERROR;
            explanation = Constants.OM3_EXPLANATION + exp.substring(0, exp.length() - 2);
        }

    }
}