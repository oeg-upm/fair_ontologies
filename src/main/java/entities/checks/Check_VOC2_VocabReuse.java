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
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Given an ontology, this check inspects its namespaces to verify other vocabs are extended/used.
 */
public class Check_VOC2_VocabReuse extends Check {
    private ArrayList<String> reusedVocabs;

    public Check_VOC2_VocabReuse(Ontology o) {
        super(o);
        this.id = Constants.VOC2;
        this.category_id = Constants.INTEROPERABLE;
        this.principle_id ="I2";
        this.description = Constants.VOC2_DESC;
        reusedVocabs = new ArrayList<>();
    }

    @Override
    public void check(){
        super.check();
        /**
         * to do:
         * - Check if things are imported.
         * - If not, check if things are extended.
         */
        //first, if there are imports, we are done.
        List<OWLImportsDeclaration> imports = this.ontology.getOntologyModel().importsDeclarations().
                collect(Collectors.toList());
        if (imports.size()>0) {
            String vocs = "";
            for (OWLImportsDeclaration imp:imports){
                vocs += imp.getIRI().getIRIString() + ", ";
            }
            vocs = vocs.substring(0, vocs.length()-2);
            total_passed_tests ++;
            status = Constants.OK;
            explanation = Constants.VOC2_EXPLANATION_OK_IMPORT+" "+vocs + ".";
        }else {
            this.getOntology().getOntologyModel().classesInSignature().forEach(a -> checkTermNamespaces(a));
            this.getOntology().getOntologyModel().objectPropertiesInSignature().forEach(a-> checkTermNamespaces(a));
            this.getOntology().getOntologyModel().dataPropertiesInSignature().forEach(a-> checkTermNamespaces(a));
            //check if other vocabularies are extended
            if(reusedVocabs.size()>0){
                this.total_passed_tests++;
                status = Constants.OK;
                String vocs = "";
                for (String v : reusedVocabs){
                    vocs += v + ", ";
                }
                vocs = vocs.substring(0, vocs.length()-2);
                explanation = Constants.VOC2_EXPLANATION_OK_EXTEND + vocs;
            }else{
                status = Constants.ERROR;
                explanation = Constants.VOC2_EXPLANATION_ERROR;
            }
        }

    }

    private void checkTermNamespaces(OWLNamedObject a){
        String termNS = a.getIRI().getNamespace();
        if(termNS.equals(Constants.NS_OWL)) return; //We ignore OWL reuse.
        if(!termNS.contains(this.ontology_URI)){
            if (!reusedVocabs.contains(termNS)) {
                this.reusedVocabs.add(termNS);
            }
        }
    }

}
