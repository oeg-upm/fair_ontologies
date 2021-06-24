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
 *
 * TO DO
 */

public class Check_VOC4_TermMetadataDescription extends Check {
    public Check_VOC4_TermMetadataDescription(Ontology o) {
        super(o);
        this.id = Constants.VOC4;
        this.category_id = Constants.REUSABLE;
        this.principle_id ="R1";
        this.description = Constants.VOC4_DESC;
    }

    @Override
    public void check(){
        super.check();
        /**
         * to do
         */
    }

}
