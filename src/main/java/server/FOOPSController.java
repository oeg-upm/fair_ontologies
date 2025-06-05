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
package server;


import com.google.gson.Gson;
import entities.Response;
import entities.ResponseResource;
import fair.FOOPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class FOOPSController {

    Logger logger = LoggerFactory.getLogger(FOOPSController.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @ApiOperation(value = "Assess GET ontology (")
    @CrossOrigin(origins = "*")
    @GetMapping("/assessOntology")
    public String assessGET() {
        return "Please send a POST request. Example: " +
                "curl -X POST \"https://foops.linkeddata.es/assessOntology\" -H \"accept: application/json;" +
                "charset=UTF-8\" " +
                "-H \"Content-Type: application/json;charset=UTF-8\" -d " +
                "\"{ \"ontURI\": \"https://w3id.org/okn/o/sd#\"}\"";
    }

    /**
     *
     * @param body String body with the JSON to parse as a request.
     * @return JSON response obtained by FOOPS
     */
    @ApiOperation( 
        value = "Assess an ontology against a set of FOOPS! tests. This is the original FOOPS! call for assessment",
        notes = "This call returns a JSON response obtained by FOOPS. To see an example, please see use the following JSON "
        + "Example request JSON:\n" 
        + "```\n" 
        + "{\n" 
        + " \"ontologyUri\": \"https://w3id.org/okn/o/sd#\"\n" 
        + "}\n" 
        + "```"
        )
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/assessOntology", consumes = "application/json", produces = "application/json")
    public String assessPOST(
        @ApiParam(value = "Ontology request object", required = true)
        @RequestBody String body) {
        Response r = null;
        FOOPS f = null;
        Path ontologyPath = null;
        
        try{
            try { //has an onto URI been provided?
                Gson gson = new Gson();
                r = gson.fromJson(body, Response.class);
                if (r.getOntologyUri() != null) {
                    f = new FOOPS(r.getOntologyUri(), false);
                }
            }catch(Exception e){
                logger.error("Error "+ e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Malformed JSON request", new Exception("Malformed JSON request"));
            }
            try{ //is there content?
                if (f == null && body!=null && !"".equals(body)){
                    ontologyPath = Path.of("ontology");
                    try (PrintWriter out = new PrintWriter(String.valueOf(ontologyPath))) {
                        out.println(body);
                    }
                    f = new FOOPS(String.valueOf(ontologyPath), true);
                }
                if (f == null){ //no ontology or content could be loaded
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Could not load ontology", new Exception("Ontology URI or ontology content not provided"));
                }
                f.fairTest();
                return f.exportJSON();
            }catch(ResponseStatusException e) {
                throw e;
            }catch(Exception e){
                logger.error("Error while processing ontology." +e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Error while processing the ontology", e);
            }finally{
                if (f != null){
                    f.removeTemporaryFolders();
                }
                if(ontologyPath != null){
                    File aux = new File(String.valueOf(ontologyPath));
                    aux.delete();
                }
            }
        }catch(ResponseStatusException e){
            throw e;
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error while processing the request", e);
        }
    }


    @ApiOperation(
            value = "Get test metadata (in JSON-LD)",
            notes = "return test description following the FTR specification."
    )
    @GetMapping(path = "/tests/{identifier}",  produces = "application/ld+json")
    public ResponseEntity<String> getTestMetadata(@PathVariable String identifier) {
        String url = "https://oeg-upm.github.io/fair_ontologies/doc/test/"+ identifier +"/"+ identifier +".jsonld" ;
        // https://w3id.org/foops/test/FIND1
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, "application/ld+json")
                .body(response.getBody());
    }


    @ApiOperation(
            value = "Runs a FOOPS! test on a resource following the FTR specification (see https://w3id.org/ftr/)",
            notes = "This call returns a JSON response obtained by FOOPS. \n"
                    + "To see all available FOOPS! tests, see https://w3id.org/foops/catalogue"
                    + "To see an example, please see use the following JSON: "
                    + "Example request JSON:\n"
                    + "```\n"
                    + "{\n"
                    + " \"resource_identifier\": \"https://w3id.org/example#\"\n"
                    + "}\n"
                    + "```"
    )
    @CrossOrigin(origins = "*")
    @PostMapping(path = "assess/test/{test_identifier}", consumes = "application/json", produces = "application/json")
    public String postTestAssessment(@PathVariable String test_identifier, @RequestBody String body) {
        String targetResource = "";
        FOOPS f = null;
        try{
            try { //has an onto URI been provided?
                Gson gson = new Gson();
                ResponseResource r = gson.fromJson(body, ResponseResource.class);
                targetResource = r.getResourceIdentifier();
                ArrayList<String> testIDs = new ArrayList<>();
                testIDs.add(test_identifier);
                f = new FOOPS(targetResource, testIDs);
                f.fairTest();
                return f.exportJSON();
            }catch(Exception e){
                logger.error("Error "+ e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Malformed JSON request", new Exception("Malformed JSON request"));
            }
        }catch(ResponseStatusException e) {
            throw e;
        }catch(Exception e){
            logger.error("Error while processing ontology." +e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while processing the ontology", e);
        }finally{
            if (f != null){
                f.removeTemporaryFolders();
            }
        }
        //return ("TO DO assessment of test "+ test_identifier + " on "+targetResource);
    }

    @ApiOperation(
            value = "Run a test on a resource",
            notes = "return test description following the FTR specification."
    )
    @PostMapping(path = "assess/benchmark/{identifier}",  produces = "text/plain")
    public String postBenchmarkAssessment(@PathVariable String identifier) {
        String url = "https://oeg-upm.github.io/fair_ontologies/doc/benchmark/"+ identifier +"/"+ identifier +".jsonld" ;
        return ("TO DO assessment of test "+ url);
    }

    @ApiOperation(
            value = "Get metric metadata (in JSON-LD)",
            notes = "return metric description following the FTR specification."
    )
    @GetMapping(path = "/metrics/{identifier}",  produces = "application/ld+json")
    public ResponseEntity<String> getMetricMetadata(@PathVariable String identifier) {
        String url = "https://oeg-upm.github.io/fair_ontologies/doc/metric/"+ identifier +"/"+ identifier +".jsonld" ;
        // https://w3id.org/foops/metric/FIND1
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, "application/ld+json")
                .body(response.getBody());
    }

    @ApiOperation(
            value = "Get benchmark metadata (in JSON-LD)",
            notes = "return benchmark descriptions following the FTR specification."
    )
    @GetMapping(path = "/benchmarks/{identifier}",  produces = "application/ld+json")
    public ResponseEntity<String> getBenchmarkMetadata(@PathVariable String identifier) {
        String url = "https://oeg-upm.github.io/fair_ontologies/doc/benchmark/"+ identifier +"/"+ identifier +".jsonld" ;
        // https://w3id.org/foops/benchmark/ALL
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, "application/ld+json")
                .body(response.getBody());
    }

    /**
     *
     * @param file file sent as part of the FormData form.
     * @param otherData String other data sent.
     * @return JSON with FOOPS! response.
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/assessOntologyFile",consumes = "multipart/form-data", produces = "application/json")
    public String assessPOST(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "otherData", required = false) String otherData) {
        FOOPS f = null;
        logger.info("Received request!");
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            try {
                File tempFile = File.createTempFile("uploaded_onto_", "_" + fileName);
                file.transferTo(tempFile);
                f = new FOOPS(tempFile.getAbsolutePath(), true);
                f.fairTest();
                tempFile.delete();
                return f.exportJSON();
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Error while processing the file", e);
            }
            finally{
                if (f != null){
                    f.removeTemporaryFolders();
                }

            }
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Could not load ontology",
                    new Exception("Ontology URI or ontology content not provided"));
        }

    }
}