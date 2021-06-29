# OEG FAIR Ontologies Validator

Authors: Daniel Garijo and María Poveda

Application to validate whether a vocabulary (OWL or SKOS) conforms with the FAIR data principles.

The client application has been integrated from the work in https://github.com/jacobomata/FairValidator. The client would not have been possible without the work from @jacobomata

## Instal instructions
The project was build and tested with JDK 11.0.11 in Ubuntu.

To create the JAR, just run:

```
mvn install
```
on the server folder, or download the JAR from the releases page.

## Running the server
Run the following command

```
java -jar -Dserver.port=PORT fair_ontologies-0.0.1.jar
```

Where PORT is the port you want to run the server

to test the installation, just do a curl command:

```
curl -X POST "http://localhost:8083/assessOntology" -H "accept: application/json;charset=UTF-8" -H "Content-Type: application/json;charset=UTF-8" -d "{ \"ontologyUri\": \"https://w3id.org/okn/o/sd\"}"
```

As a result, you should see a JSON in your console

