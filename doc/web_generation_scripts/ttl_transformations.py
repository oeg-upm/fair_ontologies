import os
import shutil
import configparser
import argparse
from rdflib import Graph
import pystache
import markdown

# query test
QUERY = """
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX ftr: <https://w3id.org/ftr#>
PREFIX dqv: <http://www.w3.org/ns/dqv#>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX doap: <http://usefulinc.com/ns/doap#>
PREFIX dpv: <https://w3id.org/dpv#> 

SELECT DISTINCT ?s ?title ?label ?description ?keywords ?version ?indicator ?label_indicator ?desc_indicator ?license
?publisher ?metric ?creator_name ?creator_orcid ?contact_orcid ?contact_name ?contact_mail ?endpoint_desc ?endpoint_url 
?applicable_for ?supported_by ?web_repository
WHERE {
    ?s a ftr:Test .
    ?s dcterms:title ?title .
    ?s rdfs:label ?label .
    ?s dcterms:description ?description .
    ?s dcterms:license ?license .
    ?s dcterms:publisher ?publisher .
    ?s dcat:keyword ?keywords .
    ?s dcat:version ?version .
    ?s ftr:indicator ?indicator .
    ?s dcat:endpointDescription ?endpoint_desc .
    ?s dcat:endpointURL ?endpoint_url .
    ?s dpv:isApplicableFor ?applicable_for .
    ?s ftr:supportedBy ?supported_by .
    ?indicator rdfs:label ?label_indicator .
    ?indicator dcterms:description ?desc_indicator .
    ?metric a dqv:Metric .
    ?repository doap:repository ?repo .
    ?repo foaf:homePage ?web_repository .
    ?s dcterms:creator ?creator_orcid .
    ?creator_orcid vcard:fn ?creator_name .
    ?s dcat:contactPoint ?contact_orcid .
    ?contact_orcid vcard:fn ?contact_name .
    ?contact_orcid vcard:hasEmail ?contact_mail .
}
"""

QUERY_METRICS = """
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX ftr: <https://w3id.org/ftr#>
PREFIX dqv: <http://www.w3.org/ns/dqv#>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX doap: <http://usefulinc.com/ns/doap#>
PREFIX dqv: <http://www.w3.org/ns/dqv#>
PREFIX dpv: <https://w3id.org/dpv#> 

SELECT DISTINCT ?s ?title ?label ?description ?keywords ?version ?license ?indimension ?label_dimension ?desc_indimension
?publisher ?test ?creator_name ?creator_orcid ?landing_page ?benchmark ?bm_title ?bm_desc ?metric_status ?contact_orcid ?contact_name 
?contact_mail ?applicable_for ?supported_by 
WHERE {
    ?s a dqv:Metric .
    ?s dcterms:title ?title .
    ?s rdfs:label ?label .
    ?s dcterms:description ?description .
    ?s dcterms:publisher ?publisher .
    ?s dcat:keyword ?keywords .
    ?s dcat:version ?version .
    ?s dcterms:license ?license .
    ?s dcat:landingPage ?landing_page .
    ?s dqv:inDimension ?indimension .
    ?s ftr:status ?metric_status .
    ?s ftr:hasBenchmark ?benchmark .
    ?s dpv:isApplicableFor ?applicable_for .
    ?s ftr:supportedBy ?supported_by .
    ?indimension rdfs:label ?label_dimension .
    ?indimension dcterms:description ?desc_indimension .
    ?benchmark a ftr:Benchmark ;
        dcterms:title ?bm_title;
        dcterms:description ?bm_desc .
    ?test a ftr:Test .
    ?s dcterms:creator ?creator_orcid .
    ?creator_orcid vcard:fn ?creator_name .
    ?s dcat:contactPoint ?contact_orcid .
    ?contact_orcid vcard:fn ?contact_name .
    ?contact_orcid vcard:hasEmail ?contact_mail .
}
"""

QUERY_BENCHMARK = """
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX ftr: <https://w3id.org/ftr#>
PREFIX dqv: <http://www.w3.org/ns/dqv#>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX doap: <http://usefulinc.com/ns/doap#>
PREFIX dqv: <http://www.w3.org/ns/dqv#>

SELECT DISTINCT ?s ?title ?label ?description ?keywords ?version ?license
 ?creator_name ?creator_orcid ?landing_page ?benchmark_status ?hasAssociatedMetric ?metricIdentifier ?metricLabel ?contact_orcid ?contact_name ?contact_mail
WHERE {
    ?s a ftr:Benchmark .
    ?s dcterms:title ?title .
    ?s rdfs:label ?label .
    ?s dcterms:description ?description .
    ?s dcat:keyword ?keywords .
    ?s dcat:version ?version .
    ?s dcterms:license ?license .
    ?s dcat:landingPage ?landing_page .
    ?s ftr:status ?benchmark_status .
    ?s dcterms:creator ?creator_orcid .
    ?creator_orcid vcard:fn ?creator_name .
    ?s ftr:hasAssociatedMetric ?hasAssociatedMetric .
    ?hasAssociatedMetric dcterms:identifier ?metricIdentifier .
    ?hasAssociatedMetric rdfs:label ?metricLabel .
    ?s dcat:contactPoint ?contact_orcid .
    ?contact_orcid vcard:fn ?contact_name .
    ?contact_orcid vcard:hasEmail ?contact_mail .
}
"""

QUERY_CATALOG_TTL = """
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX ftr: <https://w3id.org/ftr#>
PREFIX dcat: <http://www.w3.org/ns/dcat#> 

SELECT DISTINCT ?s ?title ?label ?version ?keywords ?license ?license_label
WHERE {
    ?s a ftr:Test .
    ?s dcterms:title ?title .
    ?s rdfs:label ?label .
    ?s dcat:version ?version .
    ?s dcat:keyword ?keywords .
    ?s dcterms:license ?license .
}
"""

QUERY_CATALOG_METRIC = """
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dqv: <http://www.w3.org/ns/dqv#>
PREFIX dcat: <http://www.w3.org/ns/dcat#> 

SELECT DISTINCT ?s ?title ?label ?version ?keywords ?license ?license_label
WHERE {
    ?s a dqv:Metric .
    ?s dcterms:title ?title .
    ?s rdfs:label ?label .
    ?s dcat:version ?version .
    ?s dcat:keyword ?keywords .
    ?s dcterms:license ?license .

}
"""

QUERY_CATALOG_BENCHMARK = """
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dqv: <http://www.w3.org/ns/dqv#>
PREFIX dcat: <http://www.w3.org/ns/dcat#> 
PREFIX ftr: <https://w3id.org/ftr#>

SELECT DISTINCT ?s ?title ?label ?version ?keywords ?license ?license_label
WHERE {
    ?s a ftr:Benchmark .
    ?s dcterms:title ?title .
    ?s rdfs:label ?label .
    ?s dcat:version ?version .
    ?s dcat:keyword ?keywords .
    ?s dcterms:license ?license .

}
"""


def ttl_to_html(path_ttl, path_mustache, pquery):
    """Create a html file from a ttl file"""
    g = Graph()
    g.parse(path_ttl, format="turtle")
    # Ejecutar la consulta
    results = g.query(pquery)

    data = {
        'test_identifier': '',
        'test_title': '',
        'test_name': '',
        'test_description': '',
        'test_keywords': '',
        'test_version': '',
        'test_uri_indicator': '',
        'test_indicator': '',
        'test_desc_indicator': '',
        'test_license': '',
        'test_publisher': '',
        'test_metric': '',
        'test_repository': '',
        'test_creators': '',
        'test_turtle': '',
        'test_contactName': '',
        'test_contactMail': '',
        'test_endpoint_desc': '',
        'test_endpoint_url': '',
        'test_applicable_for': '',
        'test_supported_by': ''
    }

    keywords = []

    # lo mismo ocurre con los creadores que son dos
    creators = []
    creators_orcid = []

    contacts = []
    contacts_orcid = []
    contacts_mail = []

    for row in results:

        data['test_identifier'] = row.s
        data['test_title'] = row.title
        data['test_name'] = row.label
        data['test_description'] = markdown.markdown(row.description)
        data['test_version'] = row.version
        data['test_uri_indicator'] = row.indicator
        data['test_indicator'] = row.label_indicator
        data['test_desc_indicator'] = row.desc_indicator
        data['test_license'] = row.license
        data['test_publisher'] = row.publisher
        data['test_metric'] = row.metric
        data['test_repository'] = row.web_repository
        data['test_turtle'] = row.label + '.ttl'
        data['test_endpoint_desc'] = row.endpoint_desc
        data['test_endpoint_url'] = row.endpoint_url
        data['test_applicable_for'] = row.applicable_for
        data['test_supported_by'] = row.supported_by

        if str(row.keywords) not in keywords:
            keywords.append(str(row.keywords))

        if str(row.creator_name) not in creators:
            creators.append(str(row.creator_name))

        if str(row.creator_orcid) not in creators_orcid:
            creators_orcid.append(str(row.creator_orcid))

        if str(row.contact_name) not in contacts:
            contacts.append(str(row.contact_name))

        if str(row.contact_orcid) not in contacts_orcid:
            contacts_orcid.append(str(row.contact_orcid))

        if str(row.contact_mail) not in contacts_mail:
            contacts_mail.append(str(row.contact_mail))

    all_keywords = ", ".join(keywords)

    # hay que hacer una transformación porque ahora tenemos dos arrays con los nombres
    # y el orcid que debe ser el a href y queremos que aparecca esto:

    result = []
    for nombre, orcid in zip(creators, creators_orcid):
        result.append(f'<a href="{orcid}" target="_blank">{nombre}</a>')

    result_contacts = []
    for nombre, mail, orcid in zip(contacts, contacts_mail, contacts_orcid):
        # clean_mail = mail.replace('mailto:', '')
        result_contacts.append(
            f'<a href="{orcid}" target="_blank">{nombre}</a> at <a href="https://www.upm.es" target="_blank">upm.es</a>')

    all_creators = ', '.join(result)
    all_contacts = ', '.join(result_contacts)

    data['test_keywords'] = all_keywords
    data['test_creators'] = all_creators
    data['test_contactPoint'] = all_contacts
    # Cargar la plantilla mustache
    with open(path_mustache, 'r', encoding="utf-8") as template_file:
        template_content = template_file.read()

    # sustituir la plantilla con los datos del diccionario
    renderer = pystache.Renderer()
    rendered_output = renderer.render(template_content, data)

    # guardamos el html. El path es el mismo que el ttl pero cambiando la extension
    path_html = os.path.splitext(path_ttl)[0] + '.html'

    with open(path_html, 'w', encoding="utf-8") as output_file:
        output_file.write(rendered_output)

    print(f'Archivo creado: {path_html}')


def ttl_to_jsonld(path_ttl):
    """Create a jsonld file from a ttl file"""
    g = Graph()
    g.parse(path_ttl, format="turtle")
    # serializmos
    jsonld_data = g.serialize(format="json-ld", indent=4)
    # guardamos el json. El path es el mismo que el ttl pero cambiando la extension
    path_jsonld = os.path.splitext(path_ttl)[0] + '.jsonld'

    with open(path_jsonld, "w", encoding="utf-8") as f:
        f.write(jsonld_data)

    print(f'Archivo creado: {path_jsonld}')


def ttl_to_html_benchmarks(path_ttl, path_mustache, pquery):
    '''
        ttl benchmark to html
    '''
    g = Graph()
    g.parse(path_ttl, format="turtle")
    # Ejecutar la consulta
    results = g.query(pquery)

    data = {
        'benchmark_identifier': '',
        'benchmark_title': '',
        'benchmark_name': '',
        'benchmark_description': '',
        'benchmark_keywords': '',
        'benchmark_version': '',
        'benchmark_license': '',
        'benchmark_creators': '',
        'benchmark_landing_page': '',
        'benchmark_metrics': '',
        'benchmark_status': '',
        'benchmark_turtle': '',
        'benchmark_contactName': '',
        'benchmark_contactMail': ''
    }

    # como hay varias keywords normalemnte, las meto en un array y
    # luego las uno en un string separadas por comas.
    keywords = []

    # lo mismo ocurre con los creadores que son dos
    creators = []
    creators_orcid = []

    metrics = []
    metrics_uri = []

    contacts = []
    contacts_orcid = []
    contacts_mail = []

    for row in results:
        data['benchmark_identifier'] = row.s
        data['benchmark_title'] = row.title
        data['benchmark_name'] = row.label
        data['benchmark_description'] = markdown.markdown(row.description)
        data['benchmark_version'] = row.version
        data['benchmark_license'] = row.license
        data['benchmark_landing_page'] = row.landing_page
        data['benchmark_status'] = row.benchmark_status
        data['benchmark_turtle'] = row.label.replace('Benchmark ', '') + '.ttl'

        if str(row.keywords) not in keywords:
            
            keywords.append(str(row.keywords))

        if str(row.creator_name) not in creators:
            creators.append(str(row.creator_name))

        if str(row.creator_orcid) not in creators_orcid:
            creators_orcid.append(str(row.creator_orcid))

        if str(row.metricIdentifier) not in metrics_uri:
            metrics_uri.append(str(row.metricIdentifier))

        if str(row.metricLabel) not in metrics:
            metrics.append(str(row.metricLabel))

        if str(row.contact_name) not in contacts:
            contacts.append(str(row.contact_name))

        if str(row.contact_orcid) not in contacts_orcid:
            contacts_orcid.append(str(row.contact_orcid))

        if str(row.contact_mail) not in contacts_mail:
            contacts_mail.append(str(row.contact_mail))

      
        all_keywords = ", ".join(keywords)

        result = []
        for nombre, orcid in zip(creators, creators_orcid):
            result.append(f'<a href="{orcid}" target="_blank">{nombre}</a>')

        result_metrics = []
        for name_metric, uri_metric in zip(metrics, metrics_uri):
            result_metrics.append(
                f'<a href="{uri_metric}" target="_blank">{name_metric}</a>')

        result_contacts = []
        for nombre, mail, orcid in zip(contacts, contacts_mail, contacts_orcid):
            clean_mail = mail.replace('mailto:', '')
            result_contacts.append(
                f'<a href="{orcid}" target="_blank">{nombre}</a> at <a href="https://www.upm.es" target="_blank">upm.es</a>')

        all_creators = ', '.join(result)
        all_metrics = ', '.join(result_metrics)
        all_contacts = ', '.join(result_contacts)

    data['benchmark_keywords'] = all_keywords
    data['benchmark_creators'] = all_creators
    data['benchmark_metrics'] = all_metrics
    data['benchmark_contactPoint'] = all_contacts

    # Cargar la plantilla mustache
    with open(path_mustache, 'r', encoding="utf-8") as template_file:
        template_content = template_file.read()

    # sustituir la plantilla con los datos del diccionario
    renderer = pystache.Renderer()
    rendered_output = renderer.render(template_content, data)

    # guardamos el html. El path es el mismo que el ttl pero cambiando la extension
    path_html = os.path.splitext(path_ttl)[0] + '.html'

    with open(path_html, 'w', encoding="utf-8") as output_file:
        output_file.write(rendered_output)

    print(f'Archivo creado: {path_html}')


def ttl_to_html_metrics(path_ttl, path_mustache, pquery):

    g = Graph()
    g.parse(path_ttl, format="turtle")

    results = g.query(pquery)

    data = {
        'metric_identifier': '',
        'metric_title': '',
        'metric_name': '',
        'metric_description': '',
        'metric_keywords': '',
        'metric_version': '',
        'metric_license': '',
        'metric_uri_inDimension': '',
        'metric_inDimension': '',
        'metric_desc_dimension': '',
        'metric_publisher': '',
        'metric_test': '',
        'metric_creators': '',
        'metric_landing_page': '',
        'metric_benchmark': '',
        'metric_benchmark_title': '',
        'metric_benchmark_desc': '',
        'metric_status': '',
        'metric_turtle': '',
        'metric_contactName': '',
        'metric_contactMail': '',
        'metric_applicable_for': '',
        'metric_supported_by': ''
    }

    # como hay varias keywords normalemnte, las meto en un array y
    # luego las uno en un string separadas por comas.
    keywords = []
    benchmarks = []
    benchmarks_title = []
    benchmarks_desc = []
    # lo mismo ocurre con los creadores que son dos
    creators = []
    creators_orcid = []

    contacts = []
    contacts_orcid = []
    contacts_mail = []

    for row in results:

        data['metric_identifier'] = row.s
        data['metric_title'] = row.title
        data['metric_name'] = row.label
        data['metric_description'] = markdown.markdown(row.description)
        data['metric_version'] = row.version
        data['metric_license'] = row.license
        data['metric_uri_inDimension'] = row.indimension
        data['metric_inDimension'] = row.label_dimension
        data['metric_desc_dimension'] = row.desc_indimension
        data['metric_publisher'] = row.publisher
        data['metric_test'] = row.test
        data['metric_landing_page'] = row.landing_page
        data['metric_status'] = row.metric_status
        data['metric_turtle'] = row.label.replace('Metric ', '') + '.ttl'
        data['metric_applicable_for'] = row.applicable_for
        data['metric_supported_by'] = row.supported_by

        if str(row.keywords) not in keywords:
            keywords.append(str(row.keywords))

        if str(row.creator_name) not in creators:
            creators.append(str(row.creator_name))

        if str(row.creator_orcid) not in creators_orcid:
            creators_orcid.append(str(row.creator_orcid))

        if str(row.benchmark) not in benchmarks:
            benchmarks.append(str(row.benchmark))
        if str(row.bm_title) not in benchmarks_title:
            benchmarks_title.append(str(row.bm_title))
        if str(row.bm_desc) not in benchmarks_desc:
            benchmarks_desc.append(str(row.bm_desc))

        if str(row.contact_name) not in contacts:
            contacts.append(str(row.contact_name))
        if str(row.contact_orcid) not in contacts_orcid:
            contacts_orcid.append(str(row.contact_orcid))
        if str(row.contact_mail) not in contacts_mail:
            contacts_mail.append(str(row.contact_mail))

    all_keywords = ", ".join(keywords)

    result = []
    for nombre, orcid in zip(creators, creators_orcid):
        result.append(f'<a href="{orcid}" target="_blank">{nombre}</a>')

    result_benchmarks = []

    for benchmark, title, desc in zip(benchmarks, benchmarks_title, benchmarks_desc):
        result_benchmarks.append(
            f'<a href="{benchmark}" target="_blank">{title}</a>: {desc}')

    result_contacts = []
    for nombre, mail, orcid in zip(contacts, contacts_mail, contacts_orcid):
        # clean_mail = mail.replace('mailto:', '')
        result_contacts.append(
            f'<a href="{orcid}" target="_blank">{nombre}</a> at <a href="https://www.upm.es" target="_blank">upm.es</a>')

    all_creators = ', '.join(result)
    all_benchmarks = '<br>'.join(result_benchmarks)
    all_contacts = ', '.join(result_contacts)

    data['metric_keywords'] = all_keywords
    data['metric_creators'] = all_creators
    data['metric_benchmarks'] = all_benchmarks
    data['metric_contactPoint'] = all_contacts
    # Cargar la plantilla mustache
    with open(path_mustache, 'r', encoding="utf-8") as template_file:
        template_content = template_file.read()

    # sustituir la plantilla con los datos del diccionario
    renderer = pystache.Renderer()
    rendered_output = renderer.render(template_content, data)

    # guardamos el html. El path es el mismo que el ttl pero cambiando la extension
    path_html = os.path.splitext(path_ttl)[0] + '.html'

    with open(path_html, 'w', encoding="utf-8") as output_file:
        output_file.write(rendered_output)

    print(f'Archivo creado: {path_html}')


def iterate_paths(path_source, path_destination, template, pquery, type_doc):
    ''' 
        iterate path to loof for ttls
    '''
    # param typeDoc
    # T : test
    # M : metric
    # B : benchmark
    match type_doc:
        case "T":
            subfolder = 'test'
        case "M":
            subfolder = 'metric'
        case "B":
            subfolder = 'benchmark'
        case _:
            print("Unknown type doc")

    path_source = os.path.join(path_source, subfolder)

    for root, _, files in os.walk(path_source):
        if root == path_source:
            continue

        for file in files:
            if file.endswith(".ttl"):
                # si encontramos el archivo ttl podemos llamar a las funciones de transformacion
                path_ttl_source = os.path.join(root, file)
                folder_name = os.path.basename(root)

                destination_path_folder = os.path.join(
                    path_destination, 'doc', subfolder, folder_name)

                os.makedirs(destination_path_folder, exist_ok=True)

                try:
                    shutil.copy(path_ttl_source, destination_path_folder)
                    print(
                        f"File {file} copied succesfully to {destination_path_folder}")
                    path_ttl = os.path.join(destination_path_folder, file)

                except Exception as e:
                    print(f"Error copying file: {file} - {e}")
                    continue

                match type_doc:
                    case "T":
                        ttl_to_html(path_ttl, template, pquery)
                    case "M":
                        ttl_to_html_metrics(path_ttl, template, pquery)
                    case "B":
                        ttl_to_html_benchmarks(path_ttl, template, pquery)
                    case _:
                        print("Unknown type doc")

                ttl_to_jsonld(path_ttl)


def catalog_process(path_mustache_catalog, path_source):
    ''' 
        init process to create catalog.html
    '''
    tests = []
    metrics = []
    benchmarks = []

    item_to_list(path_source, tests, QUERY_CATALOG_TTL, "T")
    item_to_list(path_source, metrics, QUERY_CATALOG_METRIC, "M")
    item_to_list(path_source, benchmarks, QUERY_CATALOG_BENCHMARK, "B")

    # # sorted list of test and metrics by name
    tests_sorted = sorted(tests, key=lambda x: x["name"])
    metrics_sorted = sorted(metrics, key=lambda x: x["name"])
    benchmarks_sorted = sorted(benchmarks, key=lambda x: x["name"])

    # extraer su uri, name y descrpción. El identificador deberá tener como href el html
    # creado en el proceso previo
    with open(path_mustache_catalog, 'r', encoding="utf-8") as template_file:
        template_content = template_file.read()

    # sustituir la plantilla con los datos del diccionario
    renderer = pystache.Renderer()
    rendered_output = renderer.render(
        template_content, {'tests': tests_sorted,
                           'metrics': metrics_sorted, 'benchmarks': benchmarks_sorted})

    path_catalog = os.path.join(path_source, 'doc', 'catalog.html')
    print("Path catalog: " + path_catalog)
    with open(path_catalog, 'w', encoding="utf-8") as output_file:
        output_file.write(rendered_output)


def item_to_list(path, plist, pquery, type_doc):

    match type_doc:
        case "T":
            subfolder = 'test'
        case "M":
            subfolder = 'metric'
        case "B":
            subfolder = 'benchmark'
        case _:
            print("Unknown type doc")

    path_source = os.path.join(path, 'doc', subfolder)

    for root, _, files in os.walk(path_source):
        for file in files:
            if file.endswith(".ttl"):
                # si encontramos el archivo ttl podemos llamar a las funciones de transformacion
                path_ttl = os.path.join(root, file)
                plist.append(ttl_to_item_catalogue(path_ttl, pquery))


def ttl_to_item_catalogue(path_ttl, pquery):
    g = Graph()
    g.parse(path_ttl, format="turtle")
    # Ejecutar la consulta
    results = g.query(pquery)

    data = {}
    keywords = []

    for row in results:

        data = {
            'identifier': row.s,
            'title': row.title,
            'name': row.label,
            'version': row.version,
            'license': row.license
        }
        # transform uri license in label license more readable
        label_license = ""

        if row.license and row.license.strip() != "":
            parts_uri = row.license.strip('/').split('/')
            if "creativecommons" in row.license.lower():
                label_license = ('CC-' + '-'.join(parts_uri[-2:])).upper()
            else:
                label_license = ('-'.join(parts_uri[-2:])).upper()

        data['license_label'] = label_license

        if str(row.keywords) not in keywords:
            keywords.append(str(row.keywords))

    all_keywords = ", ".join(keywords)
    data['keywords'] = all_keywords

    return data


def main():
    ''' 
        init function
    '''
    parser = argparse.ArgumentParser(description="Script managed files .ttl")
    parser.add_argument('-i', help="Source path of ttls", required=True)
    parser.add_argument(
        '-o', help="Destination path of files ttl, html and json-ld", required=False)

    # Cargar la configuración
    config = configparser.ConfigParser()
    config.read('config.ini')
    current_dir = os.path.dirname(os.path.abspath(__file__))
    # Simular los argumentos de línea de comandos para la depuración
    # if len(sys.argv) == 1:
    #     # No se han proporcionado argumentos
    #     sys.argv.extend(['-i', '/Users/mbp_jjm/Documents/DOCUMENTACION UPM/Fair_Ontologies/doc',
    #                     '-o', '/Users/mbp_jjm/Documents/DOCUMENTACION UPM'])

    args = parser.parse_args()
    path_source = args.i
    path_destination = args.o

    print(f"Using path_source: {path_source}")
    print(f"Using path_destination: {path_destination}")

    path_mustache_test = os.path.join(
        current_dir, "templates/template_test.html")
    path_mustache_metrics = os.path.join(
        current_dir, "templates/template_metrics.html")
    path_mustache_benchmarks = os.path.join(
        current_dir, "templates/template_benchmark.html")
    path_mustache_catalogo = os.path.join(
        current_dir, "templates/template_catalog.html")

    iterate_paths(path_source, path_destination,
                  path_mustache_metrics, QUERY_METRICS, 'M')
    iterate_paths(path_source, path_destination,
                  path_mustache_test, QUERY, 'T')
    iterate_paths(path_source, path_destination,
                  path_mustache_benchmarks, QUERY_BENCHMARK, 'B')

    # una vez hechos los test, metricas y benchmark podemos hacer el catálogo en
    # lugar de hacerlo en dos scripts diferentes
    # enviamos el path_destino porque ya se deberían haber creado allí todos los documentos.

    catalog_process(path_mustache_catalogo, path_destination)


if __name__ == "__main__":
    main()
