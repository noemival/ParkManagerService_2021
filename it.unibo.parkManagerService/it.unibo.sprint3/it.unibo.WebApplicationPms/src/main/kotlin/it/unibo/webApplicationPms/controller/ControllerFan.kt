package it.unibo.webApplicationPms.controller

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import it.unibo.webApplicationPms.connQak.CoapSupport
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.HtmlUtils


@Controller
class ControllerFan {

    var coap    = CoapSupport("coap://localhost:8021", "ctxparkingarea/fan")

    init{
          sysUtil.colorPrint(" | INIT", Color.GREEN)
            try {
                coap.observeResource( FanCoapHandler(this) )
            }catch(e:Exception){
                sysUtil.colorPrint("FanCoapHandler | ERROR=coapConnection", Color.RED)
            }

      }

     @ResponseBody
      @PostMapping("/fandata")
      fun  handleSonarValue(@RequestParam(name="info", required=false, defaultValue="0")v : String){
          coap.updateResourceWithValue(v)
      }

    }
