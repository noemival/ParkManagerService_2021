package it.unibo.webApplicationPms.controller

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket


/*
An object of this class is registered as observer of the resource
 */
    class FanCoapHandler(val controller: ControllerFan) : CoapHandler {
    var counter = 0
    val welcomeSocket = ServerSocket(6090)
    var connection = false
    var connectionSocket: Socket = Socket()
    override fun onLoad(response: CoapResponse) {
        if (connection == false){
            connectionSocket = welcomeSocket.accept()
            connection = true
        }
        var outToClient =PrintStream(connectionSocket.getOutputStream())
        val content: String = response.getResponseText()
        sysUtil.colorPrint("FanCoapHandler | response content=$content}", Color.GREEN )
        try {
            val jsonContent = JSONObject(content)
            if (jsonContent.has("info")){
                val fanRep = ResourceRep("" + HtmlUtils.htmlEscape( jsonContent.getString("info"))  )
                sysUtil.colorPrint("FanCoapHandler | fan value=${fanRep.content}", Color.BLUE)
                outToClient.println("${fanRep.content}");
                return
            }
        }catch(e:Exception){
            sysUtil.colorPrint("FanCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("FanCoapHandler  |  FAILED  ")
    }
}