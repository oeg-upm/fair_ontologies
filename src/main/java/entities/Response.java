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

package entities;

/**
 * Class representing a JSON response accepted by FOOPS.
 * We accept from URI or from file
 */
public class Response {
    private String ontologyUri;

    public String getOntologyUri() {
        return ontologyUri;
    }

    public void setOntologyUri(String ontologyUri) {
        this.ontologyUri = ontologyUri;
    }
}


