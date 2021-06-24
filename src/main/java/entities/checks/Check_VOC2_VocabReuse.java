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
 * Given an ontology, this check inspects its namespaces to verify other vocabs are extended/used.
 */
public class Check_VOC2_VocabReuse extends Check {
    public Check_VOC2_VocabReuse(Ontology o) {
        super(o);
        this.id = Constants.VOC2;
        this.category_id = Constants.INTEROPERABLE;
        this.principle_id ="I2";
        this.description = Constants.VOC2_DESC;
    }

    @Override
    public void check(){
        super.check();
        /**
         * to do:
         * - Check if things are imported.
         * - If not, check if things are extended.
         */
    }
}
