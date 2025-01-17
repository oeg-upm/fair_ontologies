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
 * This check verifies whether there is an IRI version, and whether the IRI version is different from the ontology URI
 * (if it's a proper version, it should be unique)
 */
public class Check_VER1_VersionIRI extends Check {
    public Check_VER1_VersionIRI(Ontology o) {
        super(o);
        this.principle_id = "F1";
        this.id = Constants.VER1_URL;
        this.title = Constants.VER1_TITLE;
        this.description = Constants.VER1_DESC;
        this.category_id = Constants.FINDABLE;
        this.total_tests_run = 2; // one for URI version availability, the second one for equivalence check
        this.abbreviation = Constants.VER1;
    }

    @Override
    public void check() {
        super.check();
        try {
            String versionIRI = this.ontology.getVersionIRI();
            String explanation;
            if (versionIRI != null && !"".equals(versionIRI)) {
                this.total_passed_tests++;
                if (!versionIRI.equals(this.ontology_URI)) {
                    this.total_passed_tests++;
                    this.status = Constants.OK;
                    this.explanation = Constants.VER1_EXPLANATION_OK;
                    return;
                } else {
                    explanation = " defined but equal to ontology id";
                }
            } else {
                explanation = " not defined";
            }
            this.status = Constants.ERROR;
            this.explanation = Constants.VER1_EXPLANATION_ERROR + explanation;
        }catch(Exception e){
            status = Constants.ERROR;
            explanation = Constants.ERROR_METADATA;
        }
    }
}
