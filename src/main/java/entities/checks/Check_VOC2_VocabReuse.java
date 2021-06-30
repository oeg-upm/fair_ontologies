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
import org.apache.tomcat.util.bcel.Const;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an ontology, this check inspects its namespaces to verify other vocabs are extended/used.
 */
public class Check_VOC2_VocabReuse extends Check {

    public Check_VOC2_VocabReuse(Ontology o) {
        super(o);
        this.id = Constants.VOC2;
        this.title = Constants.VOC2_TITLE;
        this.category_id = Constants.INTEROPERABLE;
        this.principle_id ="I2";
        this.description = Constants.VOC2_DESC;
    }

    @Override
    public void check(){
        super.check();
        try{
            //first, if there are imports, we are done.
            List<OWLImportsDeclaration> imports = this.ontology.getImportedVocabularies();
            this.reference_resources = new ArrayList<>();
            if (imports.size()>0) {
                for (OWLImportsDeclaration imp:imports){
                    reference_resources.add(imp.getIRI().getIRIString());
                }
                total_passed_tests ++;
                status = Constants.OK;
                explanation = Constants.VOC2_EXPLANATION_OK_IMPORT;
            }else {
                reference_resources = this.ontology.getReusedVocabularies();
                //check if other vocabularies are extended
                if (reference_resources.size() > 0) {
                    this.total_passed_tests++;
                    status = Constants.OK;
                    explanation = Constants.VOC2_EXPLANATION_OK_EXTEND;
                } else {
                    status = Constants.ERROR;
                    explanation = Constants.ERROR_VOC;
                }
            }
        }catch(Exception e){
            status = Constants.ERROR;
            explanation = Constants.VOC2_EXPLANATION_ERROR;
        }
        //to avoid returning empty lists
        if(reference_resources.size() == 0){
            reference_resources = null;
        }else{
            for(String f : Constants.FOUNDATIONAL_ONTOLOGIES){
                ArrayList<String> imported = null;
                if(!ontology.getImportedVocabularies().isEmpty()){
                    imported = reference_resources;
                }
                ArrayList<String> reused = ontology.getReusedVocabularies();
                if ((imported!= null && imported.contains(f)) || reused.contains(f)){
                    explanation += ". Foundational ontologies extended. Nicely done!";
                    break;
                }
            }
        }
    }



}
