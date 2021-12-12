package it.unibo.webApplicationPms



import it.unibo.kactor.ApplMessage
import it.unibo.robotService.ApplMsgs
import it.unibo.webApplicationPms.connQak.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

import it.unibo.webApplicationPms.email.EmailServiceImp
import it.unibo.webApplicationPms.utility.*

@kotlinx.coroutines.ObsoleteCoroutinesApi

@Controller
@RequestMapping("/")
class ControllerPms( private val emailServiceImp: EmailServiceImp) {
    var parkManagerServiceObserver: CoapObserver? = null
    lateinit var coapsupport: CoapSupport
    lateinit var connToPms: connQakBase
    var answer = ""

    var appConnection : AppConnection = AppConnection()
    var buttoncarenter : StateButtonIn = StateButtonIn.instance

    var tokenArray = arrayOfNulls<String>(7)
    //var linkedfirst :Boolean= true

    @RequestMapping("", method = arrayOf(RequestMethod.GET))
    fun index(): String {
        return "index.html"
    }

    @RequestMapping("/carenter", method = arrayOf(RequestMethod.GET))
    fun carEnter(button: Button, slotnum: Slotnum): String {
        var slot = getSlotNum()
        println(slot)

        if (!slot.equals("0") && !slot.equals("9")) {
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
    fun getTokenId(@RequestParam slotnum : String, email: User): String {
        var ans = ""
        println(slotnum)

        var tokenid : Tokenid = Tokenid()
        if (slotnum.toInt() != 0) {
            ans = appConnection.sendRequest("carenter", "carenter($slotnum)", "parkmanagerservice")

            var token = ApplMessage(ans)

            var input = token.msgContent()//ans.substring(69, 83)
            tokenid.tokenid=  input.drop(8)
            tokenid.tokenid=   tokenid.tokenid.replace(")","")

            tokenArray.set(slotnum.toInt(), tokenid.tokenid)
            if (tokenid.tokenid.equals("8") || tokenid.tokenid.equals("9")  ) {
                return "error.html"
        }else {
                emailServiceImp.sendEmail(email.email, tokenid.tokenid)
            }

        }
        return "thanks.html"
    }

    @RequestMapping("/thanks", method = arrayOf(RequestMethod.GET))
    fun thanksToken(@RequestParam tokenid: String ): String {
      if(tokenid!=null){
        var ans=""
        println("$tokenid")
        val slotnum=tokenid.get(0).toString()
        if(tokenArray[slotnum.toInt()].equals(tokenid)){
            ans = appConnection.sendRequest("pickup","pickup($tokenid)","parkmanagerservice")
            tokenArray[slotnum.toInt()]=null
        }
      if(checkAnswer(ans)){
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


    fun getSlotNum(): String {
        answer= appConnection.sendRequest("notifyIn","notifyIn(A)","parkmanagerservice")
       println(answer)
        var slotNum = answer.get(71)
        return slotNum.toString()
    }

    fun checkAnswer(ans: String):Boolean{
        //pickup
        var answer= ans.get(59).toString()
        var intans=answer.toInt()
        if(intans==1){
            print(answer)
            return true
        }else return false
    }

//TEST VERIFCATION
@ResponseBody
@RequestMapping("/carentertest", method = arrayOf(RequestMethod.GET))
fun carEntertest(button: Button, slotnum: Slotnum): String {
    var slot = getSlotNum()

    if (slot.equals("0")) {
        button.enterdisabled = false

    } else {
        if(slot.equals("9")){
            return "9"
        }else{
            button.enterdisabled = true
            slotnum.slotnum = slot.toInt()
            println(slotnum.slotnum)
        }

    }
    return slotnum.slotnum.toString()
}

    @ResponseBody
    @RequestMapping("/tokenidtest", method = arrayOf(RequestMethod.POST))
    fun getTokenIdtest(@RequestParam slotnum : Int): String {
        var ans = ""
        println(slotnum)
        println(buttoncarenter.statebuttoncarenter)
        println(slotnum)

        var tokenid : Tokenid = Tokenid()
        println(buttoncarenter.statebuttoncarenter)
        if (slotnum != 0 && buttoncarenter.statebuttoncarenter.equals("Enabled")) {
            ans = appConnection.sendRequest("carenter","carenter($slotnum)","parkmanagerservice")
            var token = ApplMessage(ans)

            var input = token.msgContent()//ans.substring(69, 83)
            tokenid.tokenid=  input.drop(8)
            tokenid.tokenid=   tokenid.tokenid.replace(")","")

            tokenArray.set(slotnum.toInt(), tokenid.tokenid)
            //emailServiceImp.sendEmail(email.email, tokenid.tokenid)
        }
        //  emailServiceImp.sendEmail(email.email,tokenid.tokenid)
        println(tokenid.tokenid)
        return tokenid.tokenid
    }

    @ResponseBody
    @RequestMapping("/thanktest", method = arrayOf(RequestMethod.GET))
    fun thanksTokentest(@RequestParam tokenid: String ): String {
        if (tokenid != null) {
            var ans = ""
            println("$tokenid")
            val slotnum = tokenid.get(0).toString()
            if (tokenArray[slotnum.toInt()].equals(tokenid)) {
                ans = appConnection.sendRequest("pickup","pickup($tokenid)","parkmanagerservice")
                tokenArray[slotnum.toInt()] = null
            }
            if (checkAnswer(ans)) {
                return "ok"
            } else {
                tokenArray.set(tokenid.get(0).toString().toInt(), tokenid)
                return "error"
            }
        } else return "ok"
    }
}