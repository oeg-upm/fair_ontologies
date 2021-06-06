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
 */

package entities.checks;

import entities.Check;
import entities.Ontology;
import fair.Constants;

public class Check_FIND1_Prefix extends Check {
    /**
     * TO DO: Check if the prefix of the ontology exists (findable)
     */

    public Check_FIND1_Prefix(Ontology o){
        super(o);
        this.id = Constants.FIND1;
        this.description = Constants.FIND1_DESC;
        this.category_id = Constants.FINDABLE;
        this.principle_id = "F3";
    }

    @Override
    public void check() {
        String prefix = this.ontology.getNamespacePrefix();
        if( prefix != null && !"".equals(prefix)){
            this.total_passed_tests++;
            this.status = Constants.OK;
            this.explanation = Constants.FIND1_EXPLANATION_OK+ ": "+prefix;
        }else{
            this.status = Constants.ERROR;
            this.explanation = Constants.FIND1_EXPLANATION_ERROR;
        }
    }
}
