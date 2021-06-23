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
 * This Check verifies whether the ontology URI used to load the ontology equals the ontology URI.
 * For example, something like https://w3id.org/blah could resolve to a file with different id.
 * (which is not a good practice)
 */

public class Check_URI2_OntologyURIEqualToID extends Check {
    private final String originalURI;

    public Check_URI2_OntologyURIEqualToID(Ontology o, String originalURI) {
        super(o);
        this.id = Constants.URI2;
        this.category_id = Constants.FINDABLE;
        this.principle_id ="F1";
        this.description = Constants.URI2_DESC;
        this.originalURI = originalURI;
    }

    @Override
    public void check() {
        super.check();
        String originalOntology = this.ontology.getOntologyURI().strip();
        if(originalOntology.endsWith("/") || originalOntology.endsWith("#")){
            originalOntology = originalOntology.substring(0, originalOntology.length()-1);
        }
        String originalURI = this.originalURI.strip();
        if (originalURI.endsWith("/") || originalURI.endsWith("#")){
            originalURI = originalURI.substring(0, originalURI.length()-1);
        }
        if (originalOntology.equals(originalURI)){
            this.total_passed_tests ++;
            status = Constants.OK;
            explanation = Constants.URI2_EXPLANATION_OK;
        }else{
            status = Constants.ERROR;
            explanation = Constants.URI2_EXPLANATION_ERROR + ". Ontology URI: "+ originalOntology
                    +". Provided URI: " + originalURI;
        }
    }
}
