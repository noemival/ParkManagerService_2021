package it.unibo.webApplicationPms


import it.unibo.kactor.MsgUtil
import it.unibo.webApplicationPms.connQak.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@kotlinx.coroutines.ObsoleteCoroutinesApi

@Controller
class ControllerPms( private val emailServiceImp: EmailServiceImp
) {
    var parkManagerServiceObserver: CoapObserver? = null
    lateinit var coapsupport: CoapSupport
    lateinit var connToPms: connQakBase
    var answer = ""

    var tokenArray = arrayOfNulls<String>(7)
    //var linkedfirst :Boolean= true



    @RequestMapping("/", method = arrayOf(RequestMethod.GET))
    fun index(): String {
        return "index.html"
    }

    @RequestMapping("/carenter", method = arrayOf(RequestMethod.GET))
    fun carenter(button: Button, slotnum: Slotnum): String {
        var slot = getSlotNum()
        if (!slot.equals("0")) {
            button.enterdisabled = true
            slotnum.slotnum = slot.toInt()
            println(slotnum.slotnum)
        } else {
            button.enterdisabled = false
        }
        return "carenter.html"
    }
    @RequestMapping("/notify",method = arrayOf(RequestMethod.GET))
    fun notifyIn( ): String {
        return "notify.html"
    }


    @RequestMapping("/tokenid", method = arrayOf(RequestMethod.POST))
    fun getTokenid(@RequestParam slotnum : Int, email: User): String {
        var ans = ""
        println(slotnum)

        var tokenid : Tokenid = Tokenid()
        if (slotnum != 0) {
            ans = createconnection("carenter", "carenter($slotnum)")
            tokenid.tokenid = ans.substring(69, 83)
            tokenArray.set(slotnum, tokenid.tokenid)
        }
        emailServiceImp.sendEmail(email.email,tokenid.tokenid)

        return "thanks.html"
    }
  /*  @GetMapping("/thanks")
    fun thanks( ): String {
        return "thanks.html"
    }*/
    @RequestMapping("/thanks", method = arrayOf(RequestMethod.GET))
    fun thankstoken(@RequestParam tokenid: String ): String {
      if(tokenid!=null){
        var ans=""
        println("$tokenid")
        val slotnum=tokenid.get(0).toString()
        if(tokenArray[slotnum.toInt()].equals(tokenid)){
           ans =createconnection("pickup", "pickup($tokenid)")

            tokenArray[slotnum.toInt()]=null
        }
      if(checkanswe(ans)){
            return "thanks.html"
        }else {
            tokenArray.set(tokenid.get(0).toString().toInt(), tokenid)
            return "error.html"
        }
      }
      else return "thanks.html"


    }

    @RequestMapping("/pickup/{tokenid}", method = arrayOf(RequestMethod.GET))
    fun pickup(@PathVariable tokenid: String, model : Model, button: Button  ): String {

        var token = Tokenid()
        token.tokenid=tokenid
        model.addAttribute("tokenid", token)

        val slotnum=tokenid.get(0).toString()

        button.pikdisabled = tokenArray[slotnum.toInt()].equals(tokenid)
        return  "pickup.html"
    }
  /*  @kotlinx.coroutines.ObsoleteCoroutinesApi
    @PostMapping( "/pms" )
    fun sendCommand(@RequestParam(name="move", required=false, defaultValue="h") moveName : String) : String {

        connToPms = connQakBase.create(ConnectionType.TCP)
        connToPms.createConnection()
        val msg = MsgUtil.buildRequest("parkmanagerserviceProxy","notifyIn",moveName, qakdestination)

        answer = connToPms.requestWithRensponse(msg)
        var slotNum = answer.get(71)

        return  slotNum.toString()
    }*/

    fun getSlotNum(): String {
        answer= createconnection("notifyIn", "notifyIn(A)")
        var slotNum = answer.get(71)
        return slotNum.toString()
    }


    fun createconnection(messageId: String, content: String): String{
        connToPms = connQakBase.create(ConnectionType.TCP)
        connToPms.createConnection()
        val msg = MsgUtil.buildRequest("parkmanagerserviceProxy", messageId, content, qakdestination)
        answer = connToPms.requestWithRensponse(msg)
        print(answer)
        return answer
    }
    fun checkanswe(ans: String):Boolean{

        var answer= ans.get(59).toString()
        var intans=answer.toInt()
        if(intans==1){
            print(answer)
            return true
        }else return false
    }

}