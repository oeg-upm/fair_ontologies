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

public class Check_OM5_1_ProvenanceMetadataBasic extends Check {

    public Check_OM5_1_ProvenanceMetadataBasic(Ontology o){
        super(o);
        this.category_id = Constants.REUSABLE;
        this.id = Constants.OM5_1;
        this.title = Constants.OM5_1_TITLE;
        this.description = Constants.OM5_1_DESC;
        this.principle_id = "R1.2";
        this.total_tests_run = Constants.PROVENANCE_METADATA_BASIC.length;
    }

    @Override
    public void check() {
        super.check();
        StringBuilder exp = new StringBuilder();
        for (String m : Constants.PROVENANCE_METADATA_BASIC) {
            if (!this.ontology.getSupportedMetadata().contains(m)) {
                exp.append(m).append(", ");
            } else {
                total_passed_tests += 1;
            }
        }
        StringBuilder optional = new StringBuilder();
        for (String m : Constants.PROVENANCE_METADATA_OPTIONAL) {
            if (!this.ontology.getSupportedMetadata().contains(m)) {
                optional.append(m).append(", ");
            }
        }
        //remove last comma
        if ("".equals(exp.toString())) {
            explanation = "All basic provenance metadata found!";
            this.status = Constants.OK;
        } else {
            this.status = Constants.ERROR;
            explanation = Constants.OM5_1_EXPLANATION + exp.substring(0, exp.length() - 2);
        }
        if (!"".equals(optional.toString())){
            explanation += "\n Warning: We could not find the following provenance metadata: "+
                    optional.substring(0,optional.length() -2) + ". Please consider adding them if appropriate.";
        }

    }
}
