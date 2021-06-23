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

public class Check_HTTP1_AccessProtocol extends Check {


    public Check_HTTP1_AccessProtocol(Ontology o) {
        super(o);
        this.id = Constants.HTTP1;
        this.description = Constants.HTTP1_DESC;
        this.category_id = Constants.ACCESSIBLE;
        this.principle_id = "A1.1";
    }

    @Override
    /**
     * Very dumb check: that an open communications protocol is used (HTTP(S)))
     */
    public void check() {
        try {
            if (this.ontology_URI.startsWith("http")) {
                this.status = Constants.OK;
                this.explanation = Constants.HTTP1_EXPLANATION_OK;
                this.total_passed_tests ++;
            }else{
                this.status = Constants.ERROR;
                this.explanation = Constants.HTTP1_EXPLANATION_ERROR;
            }
        }catch(Exception e){
            this.status = Constants.ERROR;
            this.explanation = Constants.HTTP1_EXPLANATION_ERROR;
        }
    }
}
