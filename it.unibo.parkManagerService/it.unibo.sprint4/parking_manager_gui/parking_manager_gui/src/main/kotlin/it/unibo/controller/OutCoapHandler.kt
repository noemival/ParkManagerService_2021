package  it.unibo.parkingmanagergui.controller

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.json.JSONObject
import org.springframework.web.util.HtmlUtils
import java.lang.Exception

/*
An object of this class is registered as observer of the resource
 */
    class OutCoapHandler(val controller: PmController) : CoapHandler {
    var outRep :ResourceRep= ResourceRep()
    override fun onLoad(response: CoapResponse) {
        val content: String = response.getResponseText()
        sysUtil.colorPrint("OutCoapHandler | response content=$content", Color.GREEN )
        //response={"sonarvalue":"D"} or {"info":"somevalue"}
        try {
            val jsonContent = JSONObject(content)
            if (jsonContent.has("alarm")){
                outRep = ResourceRep("" + HtmlUtils.htmlEscape( jsonContent.getString("alarm"))  )
                sysUtil.colorPrint("OutCoapHandler | temp=${outRep.content}", Color.BLUE)

                controller.simpMessagingTemplate?.convertAndSend("/topic/alarmdisplay", outRep)
                return
            }

        }catch(e:Exception){
            sysUtil.colorPrint("OutCoapHandler | ERROR=${content}", Color.RED)
        }
    }

    override fun onError() {
        System.err.println("OutCoapHandler  |  FAILED  ")
    }
}