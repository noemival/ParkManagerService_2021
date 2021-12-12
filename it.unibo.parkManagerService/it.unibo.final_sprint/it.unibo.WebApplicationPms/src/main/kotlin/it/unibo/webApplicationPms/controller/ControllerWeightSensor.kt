package it.unibo.webApplicationPms.controller

import it.unibo.kactor.*
import it.unibo.webApplicationPms.connQak.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import com.hivemq.client.mqtt.datatypes.MqttQos.AT_LEAST_ONCE
import de.smartsquare.starter.mqtt.MqttSubscribe
import it.unibo.webApplicationPms.utility.StateButtonIn

@kotlinx.coroutines.ObsoleteCoroutinesApi

@Controller

class ControllerWeightSensor {

    var buttoncarenter : StateButtonIn = StateButtonIn.instance

    lateinit var connToPms: connQakBase
    var answer = ""
    var appConnection : AppConnection = AppConnection()

    @MqttSubscribe(topic = "weightsensor/data" , qos = AT_LEAST_ONCE)
    fun subscribe ( payload:String) {


        var message = ApplMessage(payload)
        var temp = message.msgContent().substring(7, 9)

        var weight = temp.replace(")", "")
        println("weight = " + weight)
        appConnection.sendForward("weight", message.msgContent(), "parkmanagerservice")
        if (weight == "0"){
            buttoncarenter.statebuttoncarenter = "Disabled"
        }
        else{
            buttoncarenter.statebuttoncarenter = "Enabled"
        }
    }
    @ResponseBody
    @RequestMapping("/getStateButton",method = arrayOf(RequestMethod.GET))
    fun getStateButton( ): String {
        println(buttoncarenter.statebuttoncarenter)
        return buttoncarenter.statebuttoncarenter.toString()
    }

}


