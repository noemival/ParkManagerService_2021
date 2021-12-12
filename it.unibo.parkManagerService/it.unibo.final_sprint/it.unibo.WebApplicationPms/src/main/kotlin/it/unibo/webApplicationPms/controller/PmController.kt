package it.unibo.webApplicationPms

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import it.unibo.kactor.MsgUtil
import it.unibo.webApplicationPms.connQak.CoapSupport
import it.unibo.webApplicationPms.connQak.ConnectionType
import it.unibo.webApplicationPms.connQak.connQakBase

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired

import webApplicationPms.PmCoapHandler

@Controller
@RequestMapping("/pm")
class PmController {

    final var coapOut   = CoapSupport("coap://localhost:8021", "ctxparkingarea/outmanager")
    final var coapPm  = CoapSupport("coap://localhost:8021", "ctxparkingarea/parkingmanager")
    var pm : PmCoapHandler = PmCoapHandler(this)
    var out : OutCoapHandler = OutCoapHandler(this)

    @Autowired
    var  simpMessagingTemplate : SimpMessagingTemplate? = null
    init{
        sysUtil.colorPrint("PmController | INIT", Color.GREEN)
      try{
            coapPm.observeResource( pm )
            coapOut.observeResource(out)
        }catch(e:Exception){
            sysUtil.colorPrint("PMCONTROLLER | ERROR=coapConnection", Color.RED)
        }


    }
    @RequestMapping("", method = arrayOf(RequestMethod.GET))
    fun entry(model: Model): String {

        sysUtil.colorPrint("HIController | entry model=$model", Color.GREEN)

        return  "pm.html"
    }

    @RequestMapping("", method = arrayOf(RequestMethod.POST))
    fun stateChange(@RequestParam (value = "but") value: String?):String{
       sendForward("stateChange","stateChange($value)","parkingmanager")
        return "pm.html"

    }
    @ResponseBody
    @RequestMapping("/test", method = arrayOf(RequestMethod.GET))
    fun pmTest(model: Model): String {

       // coapPm.observeResource( pm )
        sysUtil.colorPrint("PmController | TEST model=$model", Color.GREEN)
        var resp= pm.trolleyRep.content+pm.fanRep.content+pm.tempRep.content
        return  resp
    }
    @ResponseBody
    @RequestMapping("/testcahnge", method = arrayOf(RequestMethod.GET))
    fun pmtestChange(model: Model): String {

       // coapPm.observeResource( pm )
        var temp = pm.tempRep.content?.replace("temp(", "");
        temp = temp?.replace(")", "")
        if(temp?.toInt()!! >35){

            sysUtil.colorPrint("HIController | test temp over", Color.BLUE)

            sendForward("stateChange","stateChange(over)","parkingmanager")

        }else{
            sysUtil.colorPrint("HIController | test temp under", Color.BLUE)

            sendForward("stateChange","stateChange(under)","parkingmanager")

        }
        sysUtil.colorPrint("PmController | TEST model=$model", Color.GREEN)
        var resp= pm.trolleyRep.content+pm.fanRep.content+pm.tempRep.content
        return  resp
    }
    @ResponseBody
    @RequestMapping("/alarm", method = arrayOf(RequestMethod.GET))
    fun alarmTest(model: Model): String {
        return out.outRep.content.toString()
    }

    fun sendForward(messageId: String, content: String, qakDestination : String){
        lateinit var connToPms: connQakBase

        connToPms = connQakBase.create(ConnectionType.TCP)
        connToPms.createConnection()

        val msg = MsgUtil.buildDispatch("parkingmanagerproxy","stateChange","$content","parkingmanager")
        sysUtil.colorPrint("HOHIII:$msg", Color.RED)

        connToPms.forward(msg)
    }
}