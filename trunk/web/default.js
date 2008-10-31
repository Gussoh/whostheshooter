var READY_STATE_UNINITIALIZED=0;
var READY_STATE_LOADING=1;
var READY_STATE_LOADED=2;
var READY_STATE_INTERACTIVE=3;
var READY_STATE_COMPLETE=4;
var CDATA_SECTION = 4;

var request = createRequest();
if( request == null)
{
    alert("XMLHttpRequest object cannot be created. Application cannot run.");
}

function createRequest(){
    var req = null;

    // Non-Microsoft browsers
    try {
        req = new XMLHttpRequest();
    } catch(ex) {
        // Microsoft browsers
        try {
            req = new ActiveXObject("Msxml2.XMLHTTP");
        } catch(ex1) {
            // Other Microsoft versions
            try {
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } catch(ex2) {
                req = null;
            }
        }
    }

    return req;
}


function loadDocument(url) {
    
    document.getElementById("loading").style.display = "block";
    request = createRequest();
    if(request == null) {
        return;
    }

    request.onreadystatechange = processRequestChange;
    request.open("GET", url, true);
    request.send(null);
    
   
}

function processRequestChange() {
    if(request == null) {
        return;
    }

    var ready = request.readyState;
    
    if(ready == READY_STATE_COMPLETE) {
        parse(request.responseXML);
    }

}

function parse(xmldata) {
    if(xmldata == null) {
        return;
    }
    
    var items = xmldata.getElementsByTagName("item");

    for(var i=0; i < items.length; i++) {
          var domObject = document.getElementById(items[i].getAttribute("id"));
          //domObject.innerHTML = items[i].firstChild.CDATASection.nodeValue;
          
          while(domObject.hasChildNodes()) {
              domObject.removeChild(domObject.firstChild);
          }
          
          var c = items[i].childNodes;
          for(var j=0; j < c.length; j++) {
              if(c[j].nodeType == CDATA_SECTION) {
                  domObject.innerHTML += c[j].nodeValue;
              }
          }
    }
    document.getElementById("loading").style.display = "none";
    
}
