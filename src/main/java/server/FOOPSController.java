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
import entities.Check;
import fair.FOOPS; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Optional;

@RestController
public class FOOPSController {

    Logger logger = LoggerFactory.getLogger(FOOPSController.class);

    @ApiOperation(value = "Assess GET  ontology")
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
        value = "Assess ontology", 
        notes = "return JSON response obtained by FOOPS. \n\n" 
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
        value = "IN CONSTRUCTION: Get test by name",
        notes = "return JSON TEST obtained by FOOPS." 
        ) 
    @GetMapping(path = "/testByName", produces = "application/json") 
    public String getTestByName( 
        @ApiParam(value = "Name of test", required = true) 
        @RequestParam String name) {

        FOOPS f = null;
        Path ontologyPath = null;
        ontologyPath = Path.of("ontology");
        f = new FOOPS(String.valueOf(ontologyPath), true); 
        Optional<Check> check = f.getTestByName(name); 
        return "{ \"check\": \"" + (check.isPresent() ? check.get().toString() : "Not found") + "\" }"; 
    }

    @ApiOperation(
        value = "IN CONSTRUCTION: Get benchmark by name",
        notes = "return JSON BENCHMARK obtained by FOOPS." 
        ) 
    @GetMapping(path = "/benchmarkByName", produces = "application/json") 
    public String getBenchmarkByName( 
        @ApiParam(value = "Name of benchmark", required = true) 
        @RequestParam String name) {

        FOOPS f = null;
        Path ontologyPath = null;
        ontologyPath = Path.of("ontology");
        f = new FOOPS(String.valueOf(ontologyPath), true); 
        Optional<Check> check = f.getBenchmarkByName(name); 
        return "{ \"check\": \"" + (check.isPresent() ? check.get().toString() : "Not found") + "\" }"; 
    }
    /**
     *
     * @param file Archivo enviado como parte del FormData.
     * @param otherData String opcional con otros datos enviados.
     * @return Respuesta JSON obtenida por FOOPS.
     */
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/assessOntologyFile",consumes = "multipart/form-data", produces = "application/json")
    public String assessPOSTe(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "otherData", required = false) String otherData) {
        FOOPS f = null;
        String fileContent = null;
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            System.out.println("file Name: " + fileName);


            try {
                fileContent = new String(file.getBytes());
                try{
                    if (f == null && fileContent!=null){
                        f = new FOOPS(fileName, true);
                    }
                    if (f == null){
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Could not load ontology", new Exception("Ontology URI or ontology content not provided"));
                    }
                    f.fairTest();
                    return f.exportJSON();

                }catch(ResponseStatusException e) {
                    throw e;
                }            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Error processing file\"}";
            }
            finally{
                if (f != null){
                    f.removeTemporaryFolders();
                }

            }
        }
        else {
            return  "{\"error\":\"No file detected\"}";
        }

    }
}