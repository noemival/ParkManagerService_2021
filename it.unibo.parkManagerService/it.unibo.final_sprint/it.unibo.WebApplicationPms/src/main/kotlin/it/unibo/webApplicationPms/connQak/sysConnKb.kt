package it.unibo.webApplicationPms.connQak

val mqtthostAddr    = "localhost"	//broker.hivemq.com
val mqttport		= "1883"
val mqtttopic       = "unibo/basicrobot"
var apphostaddress   = "localhost" //   172.17.0.2 "192.168.1.5" "localhost"
val apphostport     	= "8021"
val qakdestination 	= "parkmanagerservice"
val ctxqakdest      = "ctxparkingarea"
val connprotocol    = ConnectionType.TCP //TCP COAP HTTP MQTT
val senderactor     = "parkmanagerserviceProxy"


//fun main(){ println("consoles") }