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
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.ArrayList;

/**
 * Given an ontology, this check inspects its namespaces to verify other vocabs are extended/used (for metadata
 * annotations).
 */
public class Check_VOC1_VocabReuseMetadata extends Check {
    public Check_VOC1_VocabReuseMetadata(Ontology o) {
        super(o);
        this.id = Constants.VOC1_URL;
        this.title = Constants.VOC1_TITLE;
        this.category_id = Constants.INTEROPERABLE;
        this.principle_id ="I2";
        this.description = Constants.VOC1_DESC;
        this.abbreviation = Constants.VOC1;
    }

    @Override
    public void check(){
        super.check();
        this.reference_resources = new ArrayList<>();
        reference_resources = ontology.getReusedMetadataVocabularies();
        if(reference_resources.size()>0){
            this.total_passed_tests++;
            status = Constants.OK;
            explanation = Constants.VOC1_EXPLANATION_OK;
        }else{
            status = Constants.ERROR;
            explanation = Constants.VOC1_EXPLANATION_ERROR;
        }
        //to avoid returning empty lists
        if(reference_resources.size() == 0){
            reference_resources = null;
        }
    }


}
