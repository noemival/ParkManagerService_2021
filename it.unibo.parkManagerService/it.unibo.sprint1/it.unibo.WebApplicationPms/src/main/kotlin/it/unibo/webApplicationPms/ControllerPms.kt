package it.unibo.webApplicationPms

import it.unibo.webApplicationPms.connQak.*
import it.unibo.kactor.*

import java.net.http.WebSocket
import it.unibo.supports.IssWsHttpKotlinSupport
import it.unibo.webApplicationPms.connQak.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*



@kotlinx.coroutines.ObsoleteCoroutinesApi
//@Controller
//@ResponseBody
@RestController
class ControllerPms{
    var parkManagerServiceObserver : CoapObserver? = null
    lateinit var coapsupport     : CoapSupport
    lateinit var connToRobot     : connQakBase
    var answer = ""
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @PostMapping( "/movetrolley" )
    fun sendCommand(@RequestParam(name="move", required=false, defaultValue="h") moveName : String) : String {

        connToRobot = connQakBase.create(ConnectionType.TCP)
        connToRobot.createConnection()
        //coapsupport = CoapSupport("coap://$robothostAddr:$robotPort",
          //  "ctxparkingarea/parkmanagerservice")
        //if(parkManagerServiceObserver == null)
           // parkManagerServiceObserver = CoapObserver("parkmanagerservice","ctxparkingarea","parkmanagerservice","8021")
        val msg = MsgUtil.buildRequest("parkmanagerserviceProxy","req",moveName, qakdestination)
       // val channelForObserver= Channel<String>()
       // parkManagerServiceObserver!!.addObserver(channelForObserver,"accepted")

        answer = connToRobot.requestWithRensponse(msg)

        /*runBlocking {
            delay(5000)
            answer = channelForObserver.receive()

        }*/

        return  answer
    }

}
