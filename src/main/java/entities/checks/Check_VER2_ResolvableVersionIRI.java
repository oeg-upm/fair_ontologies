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
 * This check verifies if the ontology uses version IRI and if that IRI is resolvable
 */
public class Check_VER2_ResolvableVersionIRI extends Check {
    public Check_VER2_ResolvableVersionIRI(Ontology o) {
        super(o);
        this.category_id = Constants.FINDABLE;
        this.principle_id = "F1";
        this.id = Constants.VER2;
        this.title = Constants.VER2_TITLE;
        this.description = Constants.VER2_DESC;
        this.category_id = Constants.FINDABLE;
    }

    @Override
    public void check() {
        super.check();
        String versionIRI = this.ontology.getVersionIRI();
        if (versionIRI !=null && !"".equals(versionIRI)){
            if(Utils.isURIResolvable(versionIRI)){
                this.status = Constants.OK;
                total_passed_tests += 1;
                this.explanation = Constants.VER2_EXPLANATION_OK;
            }else{
                this.status = Constants.ERROR;
                this.explanation = Constants.VER2_EXPLANATION_ERROR;
            }
        }else{
            this.status = Constants.ERROR;
            this.explanation = Constants.VER2_EXPLANATION_ERROR_NOT_AVAILABLE;
        }
    }
}
