package it.unibo.webApplicationPms.controller


import com.hivemq.client.mqtt.datatypes.MqttQos.*
import org.springframework.web.bind.annotation.*

import de.smartsquare.starter.mqtt.MqttSubscribe
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.webApplicationPms.connQak.AppConnection
//import it.unibo.webApplicationPms.connQak.AppConnection
import it.unibo.webApplicationPms.connQak.ConnectionType
import it.unibo.webApplicationPms.connQak.connQakBase
import org.springframework.stereotype.Controller

@Controller
class ControllerThermometer {

    var appConnection : AppConnection = AppConnection()

    @MqttSubscribe(topic = "thermometer/data" , qos = AT_MOST_ONCE)
    fun subscribe ( payload:String) {

        var message = ApplMessage(payload)
        println( "temperature = "+message.msgContent() )
        appConnection.sendForward(message.msgId(), message.msgContent(),"thermometer")

    }

}
