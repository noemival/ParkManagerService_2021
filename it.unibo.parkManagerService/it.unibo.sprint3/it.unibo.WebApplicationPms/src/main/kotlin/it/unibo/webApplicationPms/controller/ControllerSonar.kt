package it.unibo.webApplicationPms.controller

import it.unibo.kactor.*
import it.unibo.webApplicationPms.connQak.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import com.hivemq.client.mqtt.datatypes.MqttQos.AT_LEAST_ONCE
import de.smartsquare.starter.mqtt.MqttSubscribe

@kotlinx.coroutines.ObsoleteCoroutinesApi

@Controller

class ControllerSonar {
    var stateButtonCarEnter = "Disabled"
    lateinit var connToPms: connQakBase
    var message = ""
    var appConnection : AppConnection = AppConnection()

    @MqttSubscribe(topic = "sonar/data" , qos = AT_LEAST_ONCE)
    fun subscribe ( payload:String) {
        var message = ApplMessage(payload)
        appConnection.sendForward("sonar", message.msgContent(),"outsonar")
        println( "distance = "+message.msgContent())

    }
}


