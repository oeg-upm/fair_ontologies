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
//import entities.Response;
//import entities.ResponseResource;
import fair.Constants;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
public class FOOPSController {

    Logger logger = LoggerFactory.getLogger(FOOPSController.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Operation(summary = "Assess GET ontology")
    @CrossOrigin(origins = "*")
    @GetMapping("/assessOntology")
    public String assessGET() {
        return "Please send a POST request. Example: " +
                "curl -X POST \"https://foops.linkeddata.es/assessOntology\" -H \"accept: application/json;" +
                "charset=UTF-8\" " +
                "-H \"Content-Type: application/json;charset=UTF-8\" -d " +
                "\"{ 'ontologyUri': 'https://w3id.org/example'}\"";
    }

    /**
     *
     * @param body String body with the JSON to parse as a request.
     * @return JSON response obtained by FOOPS
     */
    @Operation( 
        summary = "Assess an ontology against a set of FOOPS! tests. This is the original FOOPS! call for assessment",
        description = "This call returns a JSON response obtained by FOOPS. To see an example, please see use the following JSON. "
        + "Example request JSON:\n" 
        + "```\n" 
        + "{\n" 
        + " \"ontologyUri\": \"https://w3id.org/example#\"\n"
        + "}\n" 
        + "```\n"
        + "Note: Tests may fail if the ontology is too large (max 50MB)\n" 
        + "or if the URI is not valid or not resolvable."

        )
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/assessOntology", consumes = "application/json", produces = "application/json")
    public String assessPOST(
        @Parameter(description = "Ontology request object", required = true)
        @RequestBody OntologyAssessmentRequestLegacy body) {
        logger.info("Received assessment request for ontology URI: " + body.getOntologyUri());  
        FOOPS f = null;
        Path ontologyPath = null;
        
        try{
            try { //has an onto URI been provided?
                if (body.getOntologyUri() != null) {
                    f = new FOOPS(body.getOntologyUri(), false);
                }
            }catch(FileTooLargeException el){
                logger.error("Error "+ el.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "File sent for assessment is too big (max 50MB)",
                        new Exception("File sent for assessment is too big (max 50MB)"));
            }
            catch(Exception e){
                logger.error("Error "+ e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Malformed JSON request", new Exception("Malformed JSON request"));
            }
            try{ //is there content?
                if (f == null){// && body!=null && !"".equals(body)){
                    try {
                        ontologyPath = Path.of("ontology");
                        f = new FOOPS(String.valueOf(ontologyPath), true);
                    }catch(FileTooLargeException el){
                        logger.error("Error "+ el.getMessage());
                        throw new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "File sent for assessment is too big (max 50MB)",
                                new Exception("File sent for assessment is too big (max 50MB)"));
                    }catch(Exception e){
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Could not load ontology", new Exception("Ontology URI or ontology content not provided"));
                    }
                }
                f.fairTest();
                return f.exportJSON();
            }catch(ResponseStatusException e) {
                throw e;
            }catch(Exception e){
                logger.error("Error while processing ontology." +e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Error while processing the ontology", e);
            }
            // finally{
            //     if (f != null){
            //         f.removeTemporaryFolders();
            //     }
            //     if(ontologyPath != null){
            //         File aux = new File(String.valueOf(ontologyPath));
            //         aux.delete();
            //     }
            // }
        }catch(ResponseStatusException e){
            throw e;
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error while processing the request", e);
        }
        finally{
            if (f != null){
                f.removeTemporaryFolders();
            }
            if(ontologyPath != null){
                File aux = new File(String.valueOf(ontologyPath));
                aux.delete();
            }
        }
    }


    @Operation(
        summary = "Get all FOOPS! test identifiers (in JSON-LD)",
        description = "Returns a JSON-LD array with all test identifiers supported by FOOPS!. " +
            "Use these identifiers to call /assess/test/{id} or /tests/{identifier}. " +
            "See the full catalogue at https://w3id.org/foops/catalogue."
    )
    @GetMapping(path = "/tests",  produces = "application/ld+json")
    public String getTests() {
        // return Constants.FULL_LIST_OF_TESTS;

         return Constants.getFullListOfTestsMetadata();
    }

    @Operation(
        summary = "Get metadata for a specific FOOPS! test (in JSON-LD)",
        description = "Returns JSON-LD metadata for a FOOPS! test following the FTR specification. " +
            "You can use this to retrieve test details before calling /assess/test/{id}. " +
            "See the full FOOPS! test catalogue at https://w3id.org/foops/catalogue " +
            "for available test identifiers."
    )
    @GetMapping(path = "/tests/{identifier}",  produces = "application/ld+json")
    public ResponseEntity<String> getTestMetadata(
        @Parameter(description = "Test identifier (e.g., CN1, URI1, OM1, FIND1). " +
        "See https://w3id.org/foops/catalogue for the full list of available tests.",
        example = "FIND1",
        required = true)
        @PathVariable String identifier) {
        //TO DO: should return the text in /doc/test/identifier/identifier.jsonld
        String url = "https://oeg-upm.github.io/fair_ontologies/doc/test/"+ identifier +"/"+ identifier +".jsonld" ;
        // https://w3id.org/foops/test/FIND1
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, "application/ld+json")
                .body(response.getBody());
    }


    @Operation(
            summary = "Method that returns how to do a POST request to /assess/test/{id}",
            description = "Returns an example POST request to run a specific test. " +
                "Replace the test identifier in the path to get the corresponding curl command. " +
                "See the full FOOPS! test catalogue at https://w3id.org/foops/catalogue."

    )
    @GetMapping(path = "assess/test/{test_identifier}")
    public String getAssessTest(
        @Parameter(description = "Test identifier (e.g., CN1, URI1, OM1, FIND1)",
        example = "FIND1",
        required = true)
        @PathVariable String test_identifier) {
        String response = "curl -X POST \"https://foops.linkeddata.es/assess/test/"+test_identifier+"\""+
                " -H  \"accept: application/json\" -H  \"Content-Type: application/json\" " +
                " -d \"{  \\\"resource_identifier\\\": \\\"https://w3id.org/example#\\\"}\"";
        return "Please send a POST request. Example: \n" + response;
    }

    @Operation(
            summary = "Runs a FOOPS! test on a resource following the FTR specification (see https://w3id.org/ftr/). ",
            description = "This call returns a JSON response obtained by FOOPS. \n"
                    + "To see all available FOOPS! tests, see https://w3id.org/foops/catalogue. \n"
                    + "To see an example, please see use the following JSON: \n"
                    + "```\n"
                    + "{\n"
                    + " \"resource_identifier\": \"https://w3id.org/example#\"\n"
                    + "}\n"
                    + "```"
                    + "Note: Tests may fail if the ontology is too large (max 50MB)."
    )
    @CrossOrigin(origins = "*")
    @PostMapping(path = "assess/test/{test_identifier}", consumes = "application/json", produces = "application/json")
    public String postTestAssessment(@PathVariable String test_identifier,
                                     @RequestBody OntologyAssessmentRequest body) {
        String targetResource = "";
        FOOPS f = null;
        try{
            try { //has an onto URI been provided?
                Gson gson = new Gson();
                targetResource = body.getResourceIdentifier();
                logger.info("Received test request - test: " + test_identifier + ", resource: " + targetResource);
                ArrayList<String> testIDs = new ArrayList<>();
                testIDs.add(test_identifier);
                f = new FOOPS(targetResource, testIDs);
                f.fairTest();
                // return f.exportJSONLD();
                return applyOstrailsStatusMapping(f.exportJSONLD());


            }catch(FileTooLargeException el){
                logger.error("Error: ontology is too big! "+ el.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "File sent for assessment is too big (max 50MB)",
                        new Exception("File sent for assessment is too big (max 50MB)"));
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
    }

    @Operation(
            summary = "Method that returns how to do a POST request to /assess/resultSet/{id}",
            description = "Returns an indication stating how to do a post request. " +
                        "Available benchmark identifiers: ALL (all FOOPS! tests) and PRE (pre-assessment benchmark for local files). " +
                        "See the full benchmark catalogue at https://w3id.org/foops/benchmark/."
    )
    @GetMapping(path = "assess/resultset/{identifier}")
    public String getAssessResultSet(
        @Parameter(description = "Benchmark identifier (ALL or PRE). " +
        "See https://w3id.org/foops/benchmark/ for details.",
        example = "ALL",
        required = true)
        @PathVariable String identifier) {
        String response = "curl -X POST \"https://foops.linkeddata.es/assess/resultset/" + identifier +"\""+
                " -H  \"accept: application/json\" -H  \"Content-Type: application/json\" " +
                " -d \"{  \\\"resource_identifier\\\": \\\"https://w3id.org/example#\\\"}\"";
        return "Please send a POST request. Example: \n" + response;
    }

    @Operation(
            summary = "Runs a set of tests on a resource, according to the metrics defined in a benchmark.",
            description = "Returns a set of test results according to the FTR specification. The result sets that may be run " +
                    "have identifiers ALL and PRE, according to the benchmark information in " +
                    "https://w3id.org/foops/benchmark/ \n"
                    + "Example request JSON:\n"
                    + "```\n"
                    + "{\n"
                    + " \"resource_identifier\": \"https://w3id.org/example#\"\n"
                    + "}\n"
                    + "```"
                    + "Note: Tests may fail if the ontology is too large (max 50MB)."
    )
    @PostMapping(path = "assess/resultset/{identifier}",  consumes = "application/json", produces = "application/json")
    public String postResultSetAssessment(@PathVariable String identifier,
                                          @RequestBody OntologyAssessmentRequest body) {
        String targetResource = "";
        FOOPS f = null;
        try{
            try {
                targetResource = body.getResourceIdentifier();
                logger.info("Received result set request - result set: " + identifier + ", resource: " + targetResource);
                f = new FOOPS(targetResource, false);
                f.fairTest();
                // return f.exportJSONLD();
                return applyOstrailsStatusMapping(f.exportJSONLD());

            }catch(FileTooLargeException el){
                logger.error("Error: ontology too big! "+ el.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "File sent for assessment is too big (max 50MB)",
                        new Exception("File sent for assessment is too big (max 50MB)"));
            }
            catch(Exception e){
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
    }

//    @Operation(
//            value = "Run an algorithm on a resource",
//            notes = "Returns the results of an algorithm for a given resource "
//    )
//    @PostMapping(path = "assess/algorithm/{identifier}",  produces = "text/plain")
//    public String postAlgorithmAssessment(@PathVariable String identifier) {
//        //String url = "https://oeg-upm.github.io/fair_ontologies/doc/benchmark/"+ identifier +"/"+ identifier +".jsonld" ;
//        return ("TO DO assessment of algorithm");// "+ url);
//    }

    @Operation(
        summary = "Get metadata for a specific FOOPS! metric (in JSON-LD)",
        description = "Returns JSON-LD metadata for a FOOPS! metric following the FTR specification. " +
            "Metric identifiers correspond to test identifiers (e.g., FIND1, URI1, CN1, OM1). " +
            "See the full FOOPS! catalogue at https://w3id.org/foops/catalogue."
    )
    @GetMapping(path = "/metrics/{identifier}",  produces = "application/ld+json")
    public ResponseEntity<String> getMetricMetadata(
        @Parameter(description = "Metric identifier (e.g., FIND1, URI1, CN1, OM1). " +
        "See https://w3id.org/foops/catalogue for the full list.",
        example = "FIND1",
        required = true)
        @PathVariable String identifier) {
        String url = "https://oeg-upm.github.io/fair_ontologies/doc/metric/"+ identifier +"/"+ identifier +".jsonld" ;
        // https://w3id.org/foops/metric/FIND1
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, "application/ld+json")
                .body(response.getBody());
    }

    @Operation(
        summary = "Get metadata for a specific FOOPS! benchmark (in JSON-LD)",
        description = "Returns JSON-LD metadata for a FOOPS! benchmark following the FTR specification. " +
            "Available benchmark identifiers: ALL (all FOOPS! tests) and PRE (pre-assessment benchmark for local files). " +
            "See the full benchmark catalogue at https://w3id.org/foops/benchmark/."
    )
    @GetMapping(path = "/benchmarks/{identifier}",  produces = "application/ld+json")
    public ResponseEntity<String> getBenchmarkMetadata(
        @Parameter(description = "Benchmark identifier (ALL or PRE). " +
        "See https://w3id.org/foops/benchmark/ for details.",
        example = "ALL",
        required = true)
        @PathVariable String identifier) {
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
    @Operation(
            summary = "Assess an ontology against a set of FOOPS! tests for pre-assessment. This is the original FOOPS! call for assessment.",
            description = "This call returns a JSON response obtained by FOOPS. " +
                        "The ontology for assessment is in the body of the POST request.\n\n" +
                        "Note: Tests may fail if the ontology is too large for complete analysis (max 50MB)."
    )
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/assessOntologyFile",consumes = "multipart/form-data", produces = "application/json")
    public String assessPOST(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "otherData", required = false) String otherData) {
        FOOPS f = null;
        File tempFile = null;
        // logger.info("Received request!");
       
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            try {
                tempFile = File.createTempFile("uploaded_onto_", "_" + fileName);
                logger.info("Received assessment request for file: " + file.getOriginalFilename() + " (size: " + file.getSize() + " bytes)");
                file.transferTo(tempFile);
                f = new FOOPS(tempFile.getAbsolutePath(), true);
                f.fairTest();
                // tempFile.delete();
                return f.exportJSON();
            } catch(FileTooLargeException el){
                logger.error("Error: ontology is too big! "+ el.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "File sent for assessment is too big (max 50MB)",
                        new Exception("File sent for assessment is too big (max 50MB)"));
            }catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Error while processing the file", e);
            }
            finally{
                if (f != null){
                    f.removeTemporaryFolders();
                }
                if (tempFile != null && tempFile.exists()) {
                    boolean deleted = tempFile.delete();
                    if (!deleted) {
                        logger.warn("Could not delete temporary file: " + tempFile.getAbsolutePath());
                    }
                }
            } 
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Could not load ontology",
                    new Exception("Ontology URI or ontology content not provided"));
        }

    }
    
    @Operation(
        summary = "Returns a benchmark score for a resource following the FTR specification.",
        description = "Returns a BenchmarkScore according to the FTR specification. " +
                "Available benchmark identifiers: ALL, PRE.\n" +
                "Example request JSON:\n" +
                "```\n" +
                "{\n" +
                " \"resource_identifier\": \"https://w3id.org/example#\"\n" +
                "}\n" +
                "```"
    )
    @CrossOrigin(origins = "*")
    @PostMapping(path = "assess/scoringAlgorithm/{identifier}", consumes = "application/json", produces = "application/json")
    public String postBenchmarkScore(@PathVariable String identifier,
                                    @RequestBody OntologyAssessmentRequest body) {
        FOOPS f = null;
        try {
            String targetResource = body.getResourceIdentifier();
            logger.info("Received benchmark score request - benchmark: " + identifier + ", resource: " + targetResource);
            f = new FOOPS(targetResource, false);
            f.fairTest();
            return applyOstrailsStatusMapping(f.exportBenchmarkScore(identifier));
        } catch (FileTooLargeException el) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File sent for assessment is too big (max 50MB)");
        } catch (Exception e) {
            logger.error("Error " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed JSON request");
        } finally {
            if (f != null) f.removeTemporaryFolders();
        }
    }

    private String applyOstrailsStatusMapping(String jsonLD) {
        return jsonLD
                .replace("\"value\": \"ok\"", "\"value\": \"pass\"")
                .replace("\"value\": \"error\"", "\"value\": \"fail\"")
                .replace("\"value\": \"fail\", \"explanation\": \"Unexpected error", "\"value\": \"error\", \"explanation\": \"Unexpected error"
                );
    }

    public static String extractTestId(String identifier) {
        if (identifier.startsWith("http://") || identifier.startsWith("https://")) {
            int lastSlashIndex = identifier.lastIndexOf('/');
            if (lastSlashIndex != -1 && lastSlashIndex < identifier.length() - 1) {
                return identifier.substring(lastSlashIndex + 1);
            }
        }
        return identifier;
    }

}