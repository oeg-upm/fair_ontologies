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
 * Given an ontology, this checks whether an RDF representation is available
 */

public class Check_RDF1_RDFAvailability extends Check {
    public Check_RDF1_RDFAvailability(Ontology o) {
        super(o);
        this.id = Constants.RDF1;
        this.title = Constants.RDF1_TITLE;
        this.category_id = Constants.INTEROPERABLE;
        this.principle_id ="I1";
        this.description = Constants.RDF1_DESC;
        this.abbreviation = Constants.RDF1; // TODO no hay enlace
    }

    @Override
    public void check() {
        super.check();
        if (this.ontology.getOntologyModel() != null){
            total_passed_tests += 1;
            this.explanation = Constants.RDF1_EXPLANATION_OK;
            this.status = Constants.OK;
        }else{
            this.explanation = Constants.RDF1_EXPLANATION_ERROR;
            this.status = Constants.ERROR;
        }
    }
}
