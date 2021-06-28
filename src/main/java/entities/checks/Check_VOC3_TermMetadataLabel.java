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
 * Given an ontology, this check will verify whether all terms are correctly annotated with label and description
 * (reusability)
 */

public class Check_VOC3_TermMetadataLabel extends Check {
    public Check_VOC3_TermMetadataLabel(Ontology o) {
        super(o);
        this.id = Constants.VOC3;
        this.title = Constants.VOC3_TITLE;
        this.category_id = Constants.REUSABLE;
        this.principle_id ="R1";
        this.description = Constants.VOC3_DESC;
    }

    @Override
    public void check(){
        super.check();
        this.total_passed_tests = ontology.getTermsWithLabel().size();
        this.total_tests_run = ontology.getTerms().size();
        if(ontology.getTerms().size() == ontology.getTermsWithLabel().size()){
            this.status = Constants.OK;
            this.explanation = Constants.VOC3_EXPLANATION_OK;
        }else{
            this.status = Constants.ERROR;
            this.explanation = Constants.VOC3_EXPLANATION_ERROR + ontology.getTermsWithLabel().size() + " out of "+
                    ontology.getTerms().size() +" terms";
        }

    }

}
