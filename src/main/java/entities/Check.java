/*
 * Copyright 2012-2013 Ontology Engineering Group, Universidad Politecnica de Madrid, Spain
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
package entities;

import java.util.ArrayList;

public class Check {
    String id;
    String categoryId;
    String status;
    float score;
    String explanation;
    ArrayList<String> affectedElements;
    /*
    "id":"SOME ID",
"category_id":"Findable",
    "principle_id": "F1.1",
    "status":"error",
    "Status_score": 0.6,
    "explanation":"this is a text explaining the result",
    "affected_elements":["URI1","URI2"]
    },
     */

    public String getCategoryId() {
        return categoryId;
    }

    public float getScore() {
        return score;
    }

    public ArrayList<String> getAffectedElements() {
        return affectedElements;
    }
}
