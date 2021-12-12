var stompClient = null;
var temp = null;
var butOver = document.getElementById('buttonOver');
var butUnder = document.getElementById("buttonUnder");
var over = false
var under = false
var flagTMax = false
var flagTMin = false

function postJQuery(themove){
var form = new FormData();
form.append("name",  "move");
form.append("value", "r");

let myForm = document.getElementById('myForm');
let formData = new FormData(myForm);


var settings = {
  "url": "http://localhost:8080/move",
  "method": "POST",
  "timeout": 0,
  "headers": {
       "Content-Type": "text/plain"
   },
  "processData": false,
  "mimeType": "multipart/form-data",
  "contentType": false,
  "data": form
};

$.ajax(settings).done(function (response) {
  //console.log(response);  //The web page
  console.log("done move:" + themove );
});

}

function setConnected(connected) {
console.log(" %%% app setConnected:" + connected );
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
      var socket = new SockJS('/it-unibo-iss');
      var stompClient = Stomp.over(socket);

      stompClient.connect({}, function (frame) {

      setStateOver("true")
      setStateUnder("true")

      setConnected(true);
        stompClient.subscribe('/topic/infodisplay', function (msg) {

             var jsonMsg = JSON.parse(msg.body).content;

             if( jsonMsg.includes("trolley") || jsonMsg.includes("fan")) {
                 if( jsonMsg.includes("trolley")) {
                     showMsg(jsonMsg, "trolleystatedisplay");
                     trolleyState = jsonMsg;
                     console.log(trolleyState)
                 }
                 if (jsonMsg.includes("fan")) {
                     showMsg(jsonMsg, "fanstatedisplay");
                      fanState = jsonMsg;
                 }

             } else {
                 showMsg(jsonMsg, "tempvaluedisplay");
                 tempState = jsonMsg;
                 temp = jsonMsg.toString().replace("temp(", '');
                 temp = temp.replace(")", '');
                 buttons()
                 setTimeout(function() {
                    if(temp>35 && flagTMax==false){
                       flagTMax = true
                       flagTMin = false
                     //  butOver.style.display = 'block';
                       alert("TEMPERATURE OVER TMAX");
                    }else if(temp<=35 && flagTMin==false){
                         flagTMax = false
                         flagTMin = true
                      //  butUnder.style.display = 'block';
                        alert("TEMPERATURE OVER TMIN");
                    };}, 20);
             }

        });
        stompClient.subscribe('/topic/alarmdisplay', function (msg) {
            var jsonMsg = JSON.parse(msg.body).content;
            if( jsonMsg.includes("alarm(occ)")) {
                document.getElementById('DTFree').innerHTML = "OUTDOORAREA OCCUPIED OVER DTFREE TIME "
            }else{
                document.getElementById('DTFree').innerHTML = "OUTDOORAREA FREE"

            }
        });
    });
}
function setStateOver(answer){
   if(answer == "false"){
        over = false
         butOver.style.display = 'block';
   }else{
        over = true
        butOver.style.display = 'none';
   }
}

function setStateUnder(answer){
   if(answer == "false"){
        under = false
        butUnder.style.display = 'block';
   }else{
        under = true
        butUnder.style.display = 'none';
   }

}
function buttons(){

    if(temp>35 ){
       butOver.style.display = 'block';
       butUnder.style.display = 'none';
    }else if(temp >0 && temp <=35) {
       butUnder.style.display = 'block';
       butOver.style.display = 'none';
    }

}
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendUpdateRequest(){
	console.log(" sendUpdateRequest "  );
    stompClient.send("/app/update", {}, JSON.stringify({'name': 'update' }));
}

function showMsg(message, outputId) {
console.log(message );
    $("#"+outputId).html( "<pre>"+message.replace(/\n/g,"<br/>")+"</pre>" );
    //$("#applmsgintable").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
         console.log(" ------- form " + e );
         //e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendRequestData(); });



//USED BY POST-BASED GUI
//$( "#sonarvalue" ).click(function() { sendRequestData( "w") });

});



