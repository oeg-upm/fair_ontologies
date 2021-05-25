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
 */

package entities.checks;

import entities.Check;
import entities.Ontology;
import fair.Constants;

public class Check_OM2_RecommendedMetadata extends Check {
    public Check_OM2_RecommendedMetadata(Ontology o) {
        super(o);
        this.category_id = Constants.FINDABLE;
        this.id = Constants.OM2;
        this.description = Constants.OM2_DESC;
        this.principle_id = "F2";
        this.total_tests_run = Constants.RECOMMENDED_METADATA.length;
    }
    /**
     * This check verifies whether the detected metadata is the recommended one
     */

    @Override
    public void check() {
        super.check();
        String exp = "";
        for (String m: Constants.RECOMMENDED_METADATA){
            if(!this.ontology.getSupportedMetadata().contains(m)){
                exp += m+", ";
            }else{
                total_passed_tests += 1;
            }
        }
        //remove last comma
        if("".equals(exp)){
            explanation = "All recommended metadata found!";
            this.status = Constants.OK;
        }else {
            this.status = Constants.ERROR;
            explanation = Constants.OM2_EXPLANATION + exp.substring(0, exp.length() - 2);
        }

    }
}
