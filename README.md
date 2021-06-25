# OEG FAIR Ontologies Validator

Authors: Daniel Garijo and María Poveda

Disclaimer: This is work in progress.

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

