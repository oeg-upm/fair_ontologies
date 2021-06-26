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
    private ArrayList<String> reusedVocabularies;
    public Check_VOC1_VocabReuseMetadata(Ontology o) {
        super(o);
        this.id = Constants.VOC1;
        this.title = Constants.VOC2_TITLE;
        this.category_id = Constants.INTEROPERABLE;
        this.principle_id ="I2";
        this.description = Constants.VOC1_DESC;
        reusedVocabularies = new ArrayList<>();
    }

    @Override
    public void check(){
        super.check();
        this.ontology.getOntologyModel().annotations().forEach(a -> checkNamespaces(a));
        if(reusedVocabularies.size()>0){
            this.total_passed_tests++;
            status = Constants.OK;
            String vocs = "";
            for (String v : reusedVocabularies){
                vocs += v + ", ";
            }
            vocs = vocs.substring(0, vocs.length()-2);
            explanation = Constants.VOC1_EXPLANATION_OK + vocs;
        }else{
            status = Constants.ERROR;
            explanation = Constants.VOC1_EXPLANATION_ERROR;
        }
    }

    private void checkNamespaces(OWLAnnotation a){
        for (String vocab: Constants.VOCS_REUSE_METADATA){
            if (a.getProperty().getIRI().getIRIString().contains(vocab)){
                if(!reusedVocabularies.contains(vocab)) {
                    reusedVocabularies.add(vocab);
                }
            }
        }
    }
}
