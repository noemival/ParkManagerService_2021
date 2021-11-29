package it.unibo.webApplicationPms.controller

import it.unibo.kactor.*
import it.unibo.webApplicationPms.connQak.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import com.hivemq.client.mqtt.datatypes.MqttQos.AT_LEAST_ONCE
import de.smartsquare.starter.mqtt.MqttSubscribe

@kotlinx.coroutines.ObsoleteCoroutinesApi

@Controller

class ControllerWeightSensor {
    var stateButtonCarEnter = "Disabled"
    lateinit var connToPms: connQakBase
    var answer = ""
    var appConnection : AppConnection = AppConnection()

    @MqttSubscribe(topic = "weightsensor/data" , qos = AT_LEAST_ONCE)
    fun subscribe ( payload:String) {
        println( "weight = "+payload )
        appConnection.sendForward("weightsensor","weight(${payload})", "weightsensor")
        stateButtonCarEnter = "Enabled"
    }
    @ResponseBody
    @RequestMapping("/getStateButton",method = arrayOf(RequestMethod.GET))
    fun getStateButton( ): String {
        println(stateButtonCarEnter)
        return stateButtonCarEnter
    }
}


