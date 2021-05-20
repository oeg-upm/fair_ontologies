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

/**
 * Checks whether a license is associated with the ontology (several properties are tested)
 *
 */

public class Check_OM4_1_License extends Check {
    public Check_OM4_1_License(Ontology o) {
        super(o);
        this.description = Constants.OM4_1_DESC;
        this.id = Constants.OM4_1;
        this.category_id = Constants.REUSABLE;
        this.principle_id ="R1.1";
    }



    public void check() {
        String license = this.ontology.getLicense();
        if (license !=null && !"".equals(license)){
            this.status = Constants.OK;
            this.explanation = Constants.OM4_1_EXPLANATION_OK;
            this.explanation += " "+license;
            this.total_passed_tests += 1;
        }
        else{
            this.status = Constants.ERROR;
            this.explanation = Constants.OM4_1_EXPLANATION_ERROR;
        }
    }

}
