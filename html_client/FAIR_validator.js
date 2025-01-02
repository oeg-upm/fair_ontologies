var typeInputSelected = "URI";


function openInput(evt, elem) {

  hideTabContent()

  deactivateSelectors()

  showSelector(evt, elem)
}

function hideTabContent(){
    // Seleccionamos todos los elementos con selector-content y los ocultamos
    var tabcontent = document.getElementsByClassName("selector-content");
    for (var i = 0; i < tabcontent.length; i++) {
      tabcontent[i].style.display = "none";
    }

}

function deactivateSelectors(){
    // Se elimina active de todos los selector
    var tablinks = document.getElementsByClassName("selector");
    for (i = 0; i < tablinks.length; i++) {
      tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
}

function showSelector(evt,elem){
    // Se muestra el contenido y se pone como active el contenido
    document.getElementById(elem).style.display = "flex";
    evt.currentTarget.className += " active";

    typeInputSelected = elem;
}

function run(object) {
  result = object

  if(result != "Error"){
    loadResults();

    showResults();
  }else{
    showError()
  }
}

function showResults() {
  // Esconder Error
  var resultBlock = document.querySelector("#error");
  resultBlock.style.display = "none";


  // Muestra resultados
  var resultBlock = document.querySelector("#test-results");
  resultBlock.style.display = "block";


}

function showError() {
  // Esconde resultados
  var resultBlock = document.querySelector("#test-results");
  resultBlock.style.display = "none";

  // Muestra error
  var resultBlock = document.querySelector("#error");
  resultBlock.style.display = "block";

}

function loadResults(){
  loadInfo(result);

  loadGrafics(result);

  loadCategory("Findable", result);
  loadCategory("Accessible", result);
  loadCategory("Interoperable", result);
  loadCategory("Reusable", result);

}

function getAverageChecks(checks){
  total = 0
  if (checks == null) {
    return 0;
  };
  for(let i = 0; i < checks.length; i++){
    total += checks[i].total_passed_tests/checks[i].total_tests_run
  }
  return total/checks.length
}


// MARIA ESTA CON ESTO PARA RECUERAR EL TOTAL Y LOS PASADOS
function getPassedChecks(checks){

  var passedChecks = 0
  var totalChecks = 0

if (checks == null) return `(` + 0 +`)`;

  for(let i = 0; i < checks.length; i++){
    passedChecks += checks[i].total_passed_tests/checks[i].total_tests_run;
    totalChecks = i+1;
  }

  // console.log("Passed vale: " + passedChecks);
  // console.log("Total vale: " + totalChecks);

   if (passedChecks % 1 != 0) {
       passedChecks = passedChecks.toFixed(2);
   };

  return `(` + passedChecks + `/` + totalChecks+ `)`;
}

function getAbsolutePassedChecks(checks){

  var passedChecks = 0


  for(let i = 0; i < checks.length; i++){
    passedChecks += checks[i].total_passed_tests;
  }

  // console.log("Passed vale: " + passedChecks);
  // console.log("Total vale: " + totalChecks);


  return passedChecks
}


function loadGrafics(result){
  var graphics = document.querySelector("#graphics");
  var graphicScore = document.querySelector("#graphicScore");
  var graphicSpider = document.querySelector("#graphicSpider");
  var popupScore = document.querySelector("#scorePopup");
  var passedChecks = 0;

  graphicScore.innerHTML = getRadialScoreHTML(result.overall_score, 1.6);
  graphicSpider.innerHTML = getSpiderGraphHTML(result);

 // checks = groupBy(result.checks, "category_id")
 // passedChecks = getAbsolutePassedChecks(checks['Findable']) + getAbsolutePassedChecks(checks['Accessible']) + getAbsolutePassedChecks(checks['Interoperable']) + getAbsolutePassedChecks(checks['Reusable']);


  //popupScore.innerHTML = `<h4>Overall score</h4>
  //                      <p>Percentage of passed tests (` + passedChecks + `/24)</p> `;


  //   graphics.innerHTML = `
  //     <div class="col-6 d-flex align-items-center justify-content-center">`
  //     + getRadialScoreHTML(result.overall_score, 1.6) +
  //     `
  //     </div>
  //     <div class="col-6 d-flex align-items-center justify-content-center pt-4">`
   //    + getSpiderGraphHTML(result) + `
   //    </div>
   //  `
}

function getSpiderGraphHTML(result){
  checks = groupBy(result.checks, "category_id")

  category_results = {
    "Findable": getAverageChecks(checks['Findable']),
    "Accessible": getAverageChecks(checks['Accessible']),
    "Interoperable": getAverageChecks(checks['Interoperable']),
    "Reusable": getAverageChecks(checks['Reusable']),
  }


  points = {
    "center": {
      "x": 57,
      "y": 50
    },
    "reusable": {
      "x": 22,
      "y": 50
    },
    "findable": {
      "x": 57,
      "y": 15
    },
    "accesible": {
      "x": 91,
      "y": 50
    },
    "interoperable": {
      "x": 57,
      "y": 85
    }
  }

  expFindable = getPassedChecks(checks['Findable']);
  // console.log("esto es lo que he calculado de findable: " + expFindable);

  expAccessible = getPassedChecks(checks['Accessible']);
  // console.log("esto es lo que he calculado de Accessible: " + expAccessible);

  expInteroperable = getPassedChecks(checks['Interoperable']);
  // console.log("esto es lo que he calculado de Interoperable: " + expInteroperable);

  expReusable = getPassedChecks(checks['Reusable']);
   // console.log("esto es lo que he calculado de Reusable: " + expReusable);


  loadCategory("Findable", result);

  loadCategory("Accessible", result);

  loadCategory("Interoperable", result);

  loadCategory("Reusable", result);



  return `
  <svg  height="200" width="250" viewBox="-120 0 300 100" transform="scale(1.8,1.8)">
  <rect x="50" y="-30" transform="rotate(45)" width="50" height="50" fill="#fff" stroke-width="1" stroke="black" />
    <line x1="22" y1="50" x2="91" y2="50" stroke-width="1" stroke="black"></line>
    <line x1="57" y1="15" x2="57" y2="85" stroke-width="1" stroke="black"></line>
    <path  fill="#428BCA4A" stroke-linecap="round" stroke-width="1" stroke="#8499B3" d="`+getSpiderDraw(points,category_results)+`"/>
    <text x="10" y="50" text-anchor="end" dy="7" font-size="10">Reusable `+ expReusable +` </text>
    <text x="57" y="0" text-anchor="middle" dy="7" font-size="10">Findable `+ expFindable +` </text>
    <text x="95" y="50" text-anchor="start" dy="7" font-size="10">Accessible `+ expAccessible +` </text>
    <text x="57" y="90" text-anchor="middle"  dy="7" font-size="10">Interoperable `+ expInteroperable +` </text>
  </svg>
  `
}

function getSpiderPoint(center, maximum, score){

  distance_x = maximum.x - center.x
  distance_y = maximum.y - center.y

  point = null

  console.log(distance_x)
  if(distance_x == 0){
    //console.log("Punto en eje y")
    point = { "x": center.x, "y": center.y + (distance_y*score) }
  }else{
    //console.log("Punto en eje x")
    point = { "x": center.x + (distance_x*score), "y": center.y }
  }

  return point.x + ` ` + point.y

}

function getSpiderDraw(points, category_results){
  // console.log(category_results.Reusable)
  return `M `+getSpiderPoint(points.center, points.reusable, category_results.Reusable)+` L `+getSpiderPoint(points.center, points.findable, category_results.Findable)+` L `+getSpiderPoint(points.center, points.accesible, category_results.Accessible)+` L `+getSpiderPoint(points.center, points.interoperable, category_results.Interoperable)+` L `+getSpiderPoint(points.center, points.reusable, category_results.Reusable)
}

function getRadialScoreHTML(score, size){
  total = 251.2
  graphic_value = total * score
  stroke = total - graphic_value

  if (score == 1){
          return `
  <svg height="100" width="100" transform="scale(`+size+`,`+size+`)">
    <circle cx="50" cy="50" r="45" fill="#FBFBFB"/>
    <path fill="none" stroke-linecap="round" stroke-width="5" stroke="#84B399"
          stroke-dasharray="`+ graphic_value +`,`+ stroke +`"
          d="M50 10
            a 40 40 0 0 1 0 80
            a 40 40 0 0 1 0 -80"/>
    <text x="50" y="50" text-anchor="middle" dy="7" font-size="20">`+Math.round(score*100)+`%</text>
  </svg>
    `
  }

      return `
  <svg height="100" width="100" transform="scale(`+size+`,`+size+`)">
    <circle cx="50" cy="50" r="45" fill="#FBFBFB"/>
    <path fill="none" stroke-linecap="round" stroke-width="5" stroke="#E65A28"
          stroke-dasharray="`+ graphic_value +`,`+ stroke +`"
          d="M50 10
            a 40 40 0 0 1 0 80
            a 40 40 0 0 1 0 -80"/>
    <text x="50" y="50" text-anchor="middle" dy="7" font-size="20">`+Math.round(score*100)+`%</text>
  </svg>

  `
}

/**
* Dummy method for returning a hardcoded JSON for testing
*/
function getResults() {

  var input = document.querySelector("#"+typeInputSelected+"_input").value

  if(input=="error"){
    return "Error"
  }

  // TO-DO
  // Aqui iría la llamada al backend

  return {
    "ontology_URI": "la uri https://w3id.org/okn/o/sd",
    "ontology_title": " probando The Software Description Ontology",
    "ontology_license": " la licencia",
    "overall_score":0.8888889,
    "checks":[
      {
        "id": "CN1",
        "principle_id": "A1",
        "category_id": "Accessible",
        "status": "ok",
        "explanation": "Ontology available in: HTML, RDF",
        "description": "Checks if the ontology URI is published following the right content negotiation for RDF and HTML",
        "total_passed_tests": 2,
        "total_tests_run": 5,
        "affected_elements": ["URI1", "URI2"],
      },
      {
        "id": "PURL1",
        "principle_id": "F1",
        "category_id": "Findable",
        "status": "ok",
        "explanation": "Ontology URI is persistent (w3id, purl, DOI, or a W3C URL)",
        "description": " Check if the ontology uses a persistent URL",
        "total_passed_tests": 3,
        "total_tests_run": 4
      },
      {
        "id": "DOC1",
        "principle_id": "R1",
        "category_id": "Reusable",
        "status": "ok",
        "explanation": "Ontology available in HTML",
        "description": "Check if the ontology has an HTML documentation",
        "total_passed_tests": 2,
        "total_tests_run": 6
      },
      {
        "id": "RDF1",
        "principle_id": "I1",
        "category_id": "Interoperable",
        "status": "ok",
        "explanation": "Ontology available in RDF",
        "description": "Check if the ontology has an RDF serialization",
        "total_passed_tests": 3,
        "total_tests_run": 3
      },
      {
        "id": "OM1",
        "principle_id": "F2",
        "category_id": "Findable",
        "status": "unchecked",
        "explanation": "All metadata found!",
        "description": "Check to see is the following  minimum metadata [title, description, license, version iri, creator, creationDate, namespace URI] are present",
        "total_passed_tests": 5,
        "total_tests_run": 6
      },
      {
        "id": "OM2",
        "principle_id": "F2",
        "category_id": "Findable",
        "status": "unchecked",
        "explanation": "The following metadata was not found: creation date, citation",
        "description": "Check to see if the following recommended metadata [NS Prefix, version info, contributor, creation date, citation] are present",
        "total_passed_tests": 3,
        "total_tests_run": 5
      },
      {
        "id": "OM4.1",
        "principle_id": "R1.1",
        "category_id": "Reusable",
        "status": "ok",
        "explanation": "A license was found http://creativecommons.org/licenses/by/2.0/",
        "description": "Check to see if there is a license associated with the ontology",
        "total_passed_tests": 1,
        "total_tests_run": 1
      },
      {
        "id": "OM4.2",
        "principle_id": "R1.1",
        "category_id": "Reusable",
        "status": "ok",
        "explanation": "License could be resolved",
        "description": "Check to see if the license is resolvable",
        "total_passed_tests": 1,
        "total_tests_run": 1
      }
    ]
    }
}

function getPrincipleDescription (principle){

  switch( principle ) {
    case "F1":
      return "(meta)data are assigned a globally unique and persistent identifier";
      break;

    case "F2":
      return "data are described with rich metadata (defined by <a href=\"#R1\">R1</a> below)";
      break;

    case "F3":
      return "metadata clearly and explicitly include the identifier of the data it describes";
      break;

    case "F4":
      return "(meta)data are registered or indexed in a searchable resource";
      break;

    case "A1":
      return "(meta)data are retrievable by their identifier using a standardized communications protocol";
      break;

    case "A1.1":
      return "the protocol is open, free, and universally implementable";
      break;

    case "A1.2":
      return "the protocol allows for an authentication and authorization procedure, where necessary";
      break;

    case "A2":
      return "metadata are accessible, even when the data are no longer available";
      break;

    case "I1":
      return "(meta)data use a formal, accessible, shared, and broadly applicable language for knowledge representation";
      break;

    case "I2":
      return "(meta)data use vocabularies that follow FAIR principles";
      break;

    case "I3":
      return "(meta)data include qualified references to other (meta)data";
      break;

    case "R1":
      return "meta(data) are richly described with a plurality of accurate and relevant attributes";
      break;

    case "R1.1":
      return "(meta)data are released with a clear and accessible data usage license";
      break;

    case "R1.2":
      return "(meta)data are associated with detailed provenance";
      break;

    case "R1.3":
      return "(meta)data meet domain-relevant community standards";
      break;


      default:
      return "I DON'T HAVE THAT PRINCIPLE";
  }
  return "I DON'T HAVE THAT PRINCIPLE";

}

function loadInfo(result) {
  var title = document.querySelector("#title");
  title.textContent = result.ontology_title

  var URI = document.querySelector("#URI-title");
  URI.textContent = result.ontology_URI

  var license = document.querySelector("#license");
  license.textContent = result.ontology_license
}

function loadCategory(category, result) {
//DIV DEL FAIR
  var checks_div = document.getElementById(category + "-checks");
  checks_div.innerHTML = getLineHTMLNoLine();
   checks_div.style ="display: none";
  checks = getCategoryChecks(category, result);
    console.log("LONGINUS"+checks.content);
  loadPrinciples(checks, checks_div);


}

function getLineHTML(){
return `
  <div class="row w-100 mx-0" style="display: block; height: 0px; margin-top: -10px;">
     <hr color="#000000">
  </div>
  `
}


function getLineHTMLNoLine(){
return `
  <div class="row w-100 mx-0" style="display: block; height: 0px; margin-top: -10px;">
  </div>
  `
}


function getCategoryChecks(category, result) {
  var checks = result.checks.filter((check) => check.category_id == category);

  return groupBy(checks, "principle_id");
}

function loadPrinciples(principles, checks_div) {
    //DIV DEL TITULO CHIQUITO
console.log("EY"+principles);
    if (principles.length == 0) {
    console.log("ANTO");
         divNuevo.classList.add("vacio");
         var vacio = document.createElement("div");
         vacio.className = "texto-check"
         vacio.innerHTML =`  <span class="texto-check">Alvaro ferrero diesel</span>`;

        // System.out.println("================================");
            divNuevo.appendChild(vacio);

    }
  for (let principle in principles) {
  console.log("GILI"+principles.length);

    var title = document.createElement("div");
    title.innerHTML = getPrincipleHTML(principle);
    checks_div.appendChild(title);
    loadChecks2(principles[principle], checks_div);
  }
}


function loadChecks2(checks, checks_div) {
let divNuevo = document.createElement("div");


  for (let i = 0; i < checks.length; i++) {

    divNuevo.classList.add("check");
    var check = document.createElement("div");
    check.className = "p-3 caja-check ";

    //divNuevo.style.display = "none";

    check.innerHTML = getCheckHTML(checks[i]);
    divNuevo.appendChild(check);
    //hideContent(checks[i].id);
  }
  checks_div.appendChild(divNuevo);
}



function loadChecks(checks, checks_div) {
  for (let i = 0; i < checks.length; i++) {
    var check = document.createElement("div");
    check.className = "p-3 caja-check ";

    check.innerHTML = getCheckHTML(checks[i]);
    checks_div.appendChild(check);
    //hideContent(checks[i].id);
  }
}

function getCheckHTML(check_info) {

  affected_URIs_HTML = ``
  reference_URIs_HTML = ``

  if("reference_resources" in check_info){
    // console.log("FOUND AFFECTED URIS FOR: " + check_info.id + "  " + check_info.principle_id);
    reference_URIs_HTML =
    `<div class="col-12 caja-affected">
      <div class="row">
        <p class="texto-affected pl-3 "> Imported/Reused URIs: </p>
      </div>`
      + getAffectedURIsHTML(check_info.reference_resources, check_info.principle_id, check_info.abbreviation) +
    `</div>`
  }

  if("affected_elements" in check_info){
    // console.log("FOUND AFFECTED URIS FOR: " + check_info.id + "  " + check_info.principle_id);
    affected_URIs_HTML = `
    <div class="row m-0">
      <p class="texto-explanation pt-3 pl-3">
      </p>
    </div>
    <div class="col-12 caja-affected">
      <div class="row">
        <p class="texto-affected pl-3"> Affected URIs: </p>
      </div>
      `
      + getAffectedURIsHTML(check_info.affected_elements, check_info.principle_id, check_info.abbreviation) +
      `
    </div>
    `
  }
  //TODO score en variable para saber porcentaje y mostrar sugerencias

   let score = (check_info.total_passed_tests / check_info.total_tests_run);
   let divTexto = "";

   // DIV DEL CHECK
   if (score == 1) {
    divTexto = `
        <div class="col-12 p-0 caja-blanca mt-2">
          <div class="row mt-2 mx-0">
            <div class="col-8">
              <span class="texto-check">
                <a href="${check_info.id}" target="_blank">
                  ${check_info.abbreviation}: ${check_info.title}
                </a>
              </span>
            </div>
            <div class="col-2">
              <div style="position: absolute; top:-30px;">
                ${getRadialScoreHTML(score, 0.5)}
              </div>
            </div>
            <div class="col-2 d-flex align-items-center justify-content-end">
              <img
                     src="assets/down-arrow.svg"
                     onclick="arrowClicked(event, '${check_info.abbreviation}')">
            </div>
          </div>
          <div class="row m-0" id="${check_info.abbreviation}" style="display: none">
            ${getLineHTML()}
            <div class="row mx-0 mt-2 w-100">
              <dl>
                <dt>Description</dt>
                <dd>${check_info.description}</dd>
                <dt>Explanation</dt>
                <dd>${check_info.explanation}</dd>
              </dl>
            </div>
            ${affected_URIs_HTML}
            ${reference_URIs_HTML}
          </div>
        </div>
      `;
   } else {
    divTexto = `
        <div class="col-12 p-0 caja-blanca mt-2">
          <div class="row mt-2 mx-0">
            <div class="col-8">
              <span class="texto-check">
                <a href="${check_info.id}" target="_blank">
                  ${check_info.abbreviation}: ${check_info.title}
                </a>
              </span>
            </div>
            <div class="col-2">
              <div style="position: absolute; top:-30px;">
                ${getRadialScoreHTML(score, 0.5)}
              </div>
            </div>
            <div class="col-2 d-flex align-items-center justify-content-end">
              <img
                     src="assets/down-arrow.svg"
                     onclick="arrowClicked(event, '${check_info.abbreviation}')">
            </div>
          </div>
          <div class="row m-0" id="${check_info.abbreviation}" style="display: none">
            ${getLineHTML()}
            <div class="row mx-0 mt-2 w-100">
              <dl>
                <dt>Description</dt>
                <dd>${check_info.description}</dd>
                <dt>Explanation</dt>
                <dd>${check_info.explanation}</dd>
              </dl>
            </div>

            <div class="row mt-2 mx-0">
                <div class="col-8">
                    <span class="texto-check">
                        Recommended Suggestions
                    </span>
                </div>

                <div class="col-2 d-flex align-items-center justify-content-end">
                    <img src="assets/down-arrow.svg" onclick="arrowClicked(event, '${check_info.abbreviation}_SUG')">
                </div>
            </div>
            <div class="row m-0" id="${check_info.abbreviation}_SUG" style="display: none">
                ${getLineHTML()}
             <div class="row mx-0 mt-2 w-100">
                    <dl>
                        <dt class="titleSuggest">Recommended Action </dt>
                        <dd>${'Try this curl command: curl -sH Accept:text/turtle -L https://w3id.org/example'}</dd>
                        <dt class="titleSuggest">Recommended Documentation </dt>
                        <dd>${'https://w3id.org/example'}</dd>
                        <dt class="titleSuggest">Affected Elements </dt>
                    </dl>
            </div>

            ${affected_URIs_HTML}
            ${reference_URIs_HTML}
          </div>
        </div>
      `;
                 // <p id = "suggest" showSuggest('RI1', 'Try this curl command: curl -sH Accept:text/turtle -L https://w3id.org/example', 'URI1, URI2')>Suggestion</p>

   }

   return divTexto;

/*
  return (
    `
    <div class="col-12 p-0 caja-blanca mt-2">
      <div class="row mt-2 mx-0">
        <div class="col-8">
          <span class="texto-check">
            `+ check_info.id +`
          </span>
        </div>
        <div class="col-2">
          <div style="position: absolute; top:-30px;">
        `+getRadialScoreHTML(check_info.total_passed_tests/check_info.total_tests_run, 0.5)+`
          </div>
        </div>
        <div class="col-2 d-flex align-items-center justify-content-end">
          <img src="assets/up-arrow.svg" onclick="arrowClicked(event, '`+check_info.id+`')">
        </div>
      </div>
      <div class="row m-0" id="`+check_info.id+`">
      `+ getLineHTML() +`
        <div class="row mx-0 mt-2 w-100">
          <p class="texto-affected pl-3"> Description: </p>
        </div>
        <div class="row m-0 w-100">
          <p class="texto-explanation pt-3 pl-3">`
          + check_info.description +
          `</p>
        </div>
        <div class="row m-0 w-100">
          <p class="texto-affected pl-3"> Explanation: </p>
        </div>
        <div class="row m-0 w-100">
          <p class="texto-explanation pt-3 pl-3">`
          + check_info.explanation +
          `</p>
        </div>
        `+ affected_URIs_HTML +`
      </div>
    </div>
  `
  );
*/
}

function getAffectedURIsHTML(URIs, principle_id, check_id){

  var html = ``;

  for (let i = 0; i < URIs.length; i++) {
    html += `<p class="texto-URI"> - <a href="` + URIs[i] + `" target="_blank">` + URIs[i] + `</a> </p>`;

    if (i == 4 && URIs.length > 9){
      html += `<div class="collapse" id="block-id-` + principle_id + check_id +`">`;
    }

  }

    if (URIs.length > 9){
      html += `</div>
          <p>
            <!-- aria-expanded attribute is mandatory -->
            <!-- bootstrap changes it to true/false on toggle -->
            <a href="#block-id-` + principle_id + check_id +`" class="btn btn-primary btn-sm" data-toggle="collapse" aria-expanded="false" aria-controls="block-id-` + principle_id + check_id +`">
              <span class="collapsed">
                Show more
              </span>
              <span class="expanded">
                Show Less
              </span>
            </a>
          </p>`;
    }

  return html;
}

function getPrincipleHTML(text) {
  // console.log("his is the text: " + text);
  //TODO nuevo span para colapsar div
  return (
    `
    <div class="row420 my-3 pl-3 d-flex justify-content-between">
      <span id="` + text + `"class="texto-principle pl-3">` +
    text + `: `+ getPrincipleDescription (text) +
    ` </span>
     <img
          src="assets/down-arrow.svg"
          onclick="expand (event)" class="arr3">
     </div>
  `
  );
}

function groupBy(objectArray, property) {
  return objectArray.reduce(function (acc, obj) {
    var key = obj[property];
    if (!acc[key]) {
      acc[key] = [];
    }
    acc[key].push(obj);
    return acc;
  }, {});
}

function arrowClicked(event, id){
    console.log(event);
  status = getArrowStatus(event)

  replaceArrow(event, status)

  if(status=="up"){
    hideContent(id);
  }else{
    showContent(id);
  }

}

function getArrowStatus(event){
  let src = event.currentTarget.src;
  if(src.includes("up-arrow.svg")){
    return "up";
  }else{
    return "down";
  }
}

function replaceArrow(event, status){
  if(status == "up"){
    event.currentTarget.src = event.currentTarget.src.replace("up-arrow.svg", "down-arrow.svg")
  }else{
    event.currentTarget.src = event.currentTarget.src.replace("down-arrow.svg", "up-arrow.svg")
  }
}

function showContent(id) {
  var resultBlock = document.querySelector("#"+id);
  resultBlock.style.display = "block";
}

function hideContent(id) {
  var resultBlock = document.querySelector("#"+id);
  resultBlock.style.display = "none";
}

function example1(uri){
  document.getElementById("URI_input").value=uri;
  }


function showSuggest(titulo, contenido, ey) {
      //var checks_div = document.getElementById(category + "-checks");
      var suggest_div = document.createElement("suggest");
    suggest_div.innerHTML = getLineHTMLNoLine();
       suggest_div.style ="display: none";
    //  checks = getCategoryChecks(category, result);

        var title = document.createElement("div");
        title.innerHTML = getPrincipleHTML(principle);
        suggest_div.appendChild(title);
      //loadChecks2(principles[principle], checks_div);

    return
      `  <div class="col-12 p-0 caja-blanca mt-2">
          <div class="row mt-2 mx-0">
            <div class="col-8">
              <span class="texto-check">
                <a href="${check_info.id}" target="_blank">
                  ${check_info.abbreviation}: ${check_info.title}
                </a>
              </span>
            </div>
            <div class="col-2">
              <div style="position: absolute; top:-30px;">
                ${getRadialScoreHTML(score, 0.5)}
              </div>
            </div>
            <div class="col-2 d-flex align-items-center justify-content-end">
              <img
                     src="assets/down-arrow.svg"
                     onclick="arrowClicked(event, '${check_info.abbreviation}')">
            </div>
          </div>
          <div class="row m-0" id="${check_info.abbreviation}" style="display: none">
            ${getLineHTML()}
            <div class="row mx-0 mt-2 w-100">
              <dl>
                <dt>Description</dt>
                <dd>${check_info.description}</dd>
                <dt>Explanation</dt>
                <dd>${check_info.explanation}</dd>
              </dl>
            </div>
            <p id = "suggest" onclick="showSuggest('RI1', 'Try this curl command: curl -sH Accept:text/turtle -L https://w3id.org/example')">Suggestion</p>
            ${affected_URIs_HTML}
            ${reference_URIs_HTML}
          </div>
        </div>
      `


}

  /**********/
// Función para crear y mostrar la ventana emergente
function showSuggest2(titulo, contenido, ey) {
  // Crear el modal (ventana emergente)
  const modal = document.createElement('div');
  modal.style.position = 'fixed';
  modal.style.zIndex = '1000';
  modal.style.left = '0';
  modal.style.top = '0';
  modal.style.width = '100%';
  modal.style.height = '100%';
  modal.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
  modal.style.display = 'flex';
  modal.style.justifyContent = 'center';
  modal.style.alignItems = 'center'; // Esto centra el modal vertical y horizontalmente

  // Crear el contenido de la ventana
  const modalContent = document.createElement('div');
  modalContent.style.backgroundColor = 'white';
  modalContent.style.padding = '20px';
  modalContent.style.borderRadius = '8px';
  modalContent.style.textAlign = 'center';
  modalContent.style.maxWidth = '500px';
  modalContent.style.width = '80%';

  // Crear el botón de cerrar (X)
  const closeButton = document.createElement('span');
  closeButton.textContent = '×';
  closeButton.style.position = 'absolute';
  closeButton.style.top = '10px';
  closeButton.style.right = '20px';
  closeButton.style.fontSize = '24px';
  closeButton.style.cursor = 'pointer';

  // Crear el título
  const tituloElemento = document.createElement('h2');
  tituloElemento.textContent = "SUGGESTION " + titulo;

   /*titulo 1*/
  // Crear el contenido "https://github.com/oeg-upm/fair_ontologies/issues"
  const contenidoElemento = document.createElement('p');

  const tituloEelemento1 = document.createElement('span');
  tituloEelemento1.classList.add('tituloSuggest');
  const contenidoTitulo1 = document.createTextNode("Action: ");
  tituloEelemento1.appendChild(contenidoTitulo1);

  let contenido1 = document.createTextNode(contenido);

  contenidoElemento.appendChild(tituloEelemento1);
  contenidoElemento.appendChild(contenido1);

  // Crear el botón de aceptar
  const aceptarBtn = document.createElement('button');
  aceptarBtn.textContent = 'Aceptar';
  aceptarBtn.style.marginTop = '20px';
  aceptarBtn.style.padding = '10px 20px';
  aceptarBtn.style.fontSize = '16px';
  aceptarBtn.style.cursor = 'pointer';
  aceptarBtn.style.backgroundColor = '#007bff';
  aceptarBtn.style.color = 'white';
  aceptarBtn.style.border = 'none';
  aceptarBtn.style.borderRadius = '4px';

  // Agregar los elementos al modal
  modalContent.appendChild(closeButton);
  modalContent.appendChild(tituloElemento);
  /*agregar contenido*/
  modalContent.appendChild(contenidoElemento);
  //modalContent.appendChild(contenidoElemento2);

 if(contenido!=null){
    // Crear el elemento contenedor
    const contenidoElemento2 = document.createElement('p');

    // Asegúrate de que "ey" tenga un valor válido
    //let ey = "Este es un texto dinámico"; // Define un valor para ey

    // Crear el nodo de texto para "ey"
    let contenido2 = document.createTextNode(contenido);

    // Crear un elemento <span> con el título
    const titulo2 = document.createElement('span');
    titulo2.classList.add('tituloSuggest');
    const contenidoTitulo2 = document.createTextNode("Recommended documentation: ");
    titulo2.appendChild(contenidoTitulo2);

    // Agregar el título y el texto al contenedor
    contenidoElemento2.appendChild(titulo2);
    contenidoElemento2.appendChild(contenido2);

    // Agregar el contenedor al modal
    modalContent.appendChild(contenidoElemento2);

  }

  /*titulo 3*/
  if(ey!=null){
    // Crear el elemento contenedor
    const contenidoElemento3 = document.createElement('p');

    // Asegúrate de que "ey" tenga un valor válido
   // let ey = "Este es un texto dinámico"; // Define un valor para ey

    // Crear el nodo de texto para "ey"
    let contenido3 = document.createTextNode(ey);

    // Crear un elemento <span> con el título
    const titulo3 = document.createElement('span');
    titulo3.classList.add('tituloSuggest');
    const contenidoTitulo3 = document.createTextNode("Affected elements: ");
    titulo3.appendChild(contenidoTitulo3);

    // Agregar el título y el texto al contenedor
    contenidoElemento3.appendChild(titulo3);
    contenidoElemento3.appendChild(contenido3);

    // Agregar el contenedor al modal
    modalContent.appendChild(contenidoElemento3);

  }

  modalContent.appendChild(aceptarBtn);
  modal.appendChild(modalContent);

  // Agregar el modal al body del documento
  document.body.appendChild(modal);

  // Manejar el clic en el botón de "Aceptar"
  aceptarBtn.onclick = function() {
    document.body.removeChild(modal); // Eliminar el modal
  };

  // Manejar el clic en la "X" para cerrar el modal
  closeButton.onclick = function() {
    document.body.removeChild(modal); // Eliminar el modal
  };

  // Cerrar el modal si se hace clic fuera del contenido
  window.onclick = function(event) {
    if (event.target === modal) {
      document.body.removeChild(modal); // Eliminar el modal
    }
  };
}

// Llamar a la función cuando sea necesario, por ejemplo, en un evento de clic en un botón
// showSuggest("Aviso importante", "Este es un mensaje personalizado.");



function expand (event) {
    let expandImg = event.target;
    let statusExpand = (expandImg.src.includes("up-arrow.svg") ? "up" : "down");
    replaceArrow(event, statusExpand);
    let divPadre = expandImg.parentNode;
    let padreDiv = divPadre.parentNode.nextElementSibling;
    Array.from(padreDiv.children).forEach(divHijo => {
      // Buscar la imagen dentro del hijo
      const img = divHijo.querySelector("img");
      if(statusExpand == "up" && img.src.includes("up-arrow.svg") ){
          img.click();
      } else if (statusExpand == "down" && img.src.includes("down-arrow.svg")) {
         img.click();
      }
    });
}




