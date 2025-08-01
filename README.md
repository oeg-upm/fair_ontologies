# OEG FAIR Ontologies Assessment (FOOPS!)
[![DOI](https://zenodo.org/badge/367292245.svg)](https://doi.org/10.5281/zenodo.14767999)

<img src="html_client/assets/foopsLogo.png" alt="logo" width="200"/>

Authors: Daniel Garijo and María Poveda, with contributions from Jacobo Mata.

FOOPS! is an application for validating whether a vocabulary (OWL or SKOS) conforms with the FAIR data principles.

Our [ISWC 2021 demo paper](html_client/assets/iswc_2021_demo.pdf) (**best demo award**) provides an overview of the FOOPS! service. Please cite our work as follows:
```
@article{foops2021,
    title        = {FOOPS!: An Ontology Pitfall Scanner for the FAIR Principles},
    author       = {Garijo, Daniel and Corcho, Oscar and Poveda-Villal{\'o}n, Mar{\i}a},
    year         = 2021,
    booktitle    = {International Semantic Web Conference (ISWC) 2021: Posters, Demos, and Industry Tracks},
    publisher    = {CEUR-WS.org},
    series       = {CEUR Workshop Proceedings},
    volume       = 2980,
    url          = {http://ceur-ws.org/Vol-2980/paper321.pdf}
}
```
If you are interested in more information, check [our slides](https://www.slideshare.net/dgarijo/foops-an-ontology-pitfall-scanner-for-the-fair-principles) with the rationale of FOOPS! and the [teaser video](https://www.youtube.com/watch?v=s8FaFl8i6yQ&ab_channel=OEG-UPM) we made for ISWC 2021.

The client application has been integrated from the work in https://github.com/jacobomata/FairValidator. The client would not have been possible without the work from @jacobomata

## Demo
A public demo of FOOPS! is available here: [https://w3id.org/foops/](https://w3id.org/foops/)

## Metrics and tests
The metrics (i.e., conceptual description of what is being assessed) and tests (i.e., implementation of a metric) supported by the tool are available at [https://w3id.org/foops/catalog](https://w3id.org/foops/catalog), along with a detail description of their rationale and means of verification.

## Installation instructions
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

As a result, you should see a JSON in your console, such as the one in sample.json.

## Running the JAR in local
To create the JAR, just run:

```
mvn install -f pom_jar.xml
```

Then run the following command to test a URI: 

```
java -jar target/fair_ontologies-0.1.0.jar -ontURI https://w3id.org/example
```

You can also test one ontology from a file:

```
java -jar target/fair_ontologies-0.1.0.jar -ontFile filePath
```

As a result, you should see a JSON in your folder, such as the one in sample.json.


If you want to change the out file path you can use the flag -out
