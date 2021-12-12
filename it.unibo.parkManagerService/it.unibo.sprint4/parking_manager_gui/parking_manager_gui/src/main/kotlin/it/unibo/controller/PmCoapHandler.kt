package  it.unibo.parkingmanagergui.controller

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import it.unibo.parkingmanagergui.configuration.WebSocketConfig

import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.lang.Exception

/*
An object of this class is registered as observer of the resource
 */
    class PmCoapHandler(val controller: PmController) : CoapHandler {
    var tempRep : ResourceRep = ResourceRep()
    var trolleyRep :ResourceRep =ResourceRep()
    var fanRep :ResourceRep=ResourceRep()
    override fun onLoad(response: CoapResponse) {
        sysUtil.colorPrint("PmCoapHandler | response content=$response", Color.GREEN )



        val content: String = response.getResponseText()
        sysUtil.colorPrint("PmCoapHandler | response content=$content", Color.GREEN )
        try {
            val jsonContent = JSONObject(content)

                tempRep = ResourceRep("" + HtmlUtils.htmlEscape( jsonContent.getString("temp"))  )
                sysUtil.colorPrint("PmCoapHandler | temp=${tempRep.content}", Color.BLUE)

                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, tempRep)
                /* The resource shows something new  */
                //sysUtil.colorPrint("WebPageCoapHandler | value: $content simpMessagingTemplate=${controller.simpMessagingTemplate}", Color.BLUE)
                 trolleyRep = ResourceRep("" + HtmlUtils.htmlEscape(jsonContent.getString("statetrolley")))
                sysUtil.colorPrint("PmCoapHandler | statetrolley=${trolleyRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, trolleyRep)

                /* The resource shows something new  */
                //sysUtil.colorPrint("WebPageCoapHandler | value: $content simpMessagingTemplate=${controller.simpMessagingTemplate}", Color.BLUE)
                 fanRep = ResourceRep("" + HtmlUtils.htmlEscape(jsonContent.getString("statefan")))
                sysUtil.colorPrint("PmCoapHandler | statefan=${fanRep.content}", Color.BLUE)
                controller.simpMessagingTemplate?.convertAndSend(WebSocketConfig.topicForClient, fanRep)
                return

        }catch(e:Exception){
            sysUtil.colorPrint("PmCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("PmCoapHandler  |  FAILED  ")
    }
}