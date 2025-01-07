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

/**
 * Given an ontology, this check verifies if its license is resolvable.
 */

public class Check_OM4_2_LicenseIsResolvable extends Check {

    public Check_OM4_2_LicenseIsResolvable(Ontology o) {
        super(o);
        this.description = Constants.OM4_2_DESC;
        this.id = Constants.OM42_URL;
        this.title = Constants.OM4_2_TITLE;
        this.category_id = Constants.REUSABLE;
        this.principle_id ="R1.1";
        this.abbreviation = Constants.OM4_2;
    }

    @Override
    public void check() {
        super.check();
        String license = this.ontology.getLicense();
        if (license !=null && !"".equals(license)){
            if(Utils.isURIResolvable(license)){
                this.status = Constants.OK;
                total_passed_tests += 1;
                this.explanation = Constants.OM4_2_EXPLANATION_OK;
            }else{
                this.status = Constants.ERROR;
                this.explanation = Constants.OM4_2_EXPLANATION_ERROR;
            }
        }else{
            this.status = Constants.ERROR;
            this.explanation = Constants.OM4_1_EXPLANATION_ERROR;
        }

    }


}
