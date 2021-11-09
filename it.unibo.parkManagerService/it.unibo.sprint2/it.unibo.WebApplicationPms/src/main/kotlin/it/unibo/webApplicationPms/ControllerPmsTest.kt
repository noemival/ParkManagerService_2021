package it.unibo.webApplicationPms


import it.unibo.kactor.MsgUtil
import it.unibo.webApplicationPms.connQak.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


/* For launching test comment annotation @Controller in ControllerPms
and decomment annotation @Controller in Controller in ControllerPmsTester */
@kotlinx.coroutines.ObsoleteCoroutinesApi
//@Controller
class ControllerPmsTest (private val emailServiceImp: EmailServiceImp){

    lateinit var connToPms: connQakBase
    var answer = ""
    var tokenArray = arrayOfNulls<String>(7)

    @ResponseBody
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
        return slotnum.slotnum.toString()
    }

    @ResponseBody
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
      //  emailServiceImp.sendEmail(email.email,tokenid.tokenid)
        println(tokenid.tokenid)
        return tokenid.tokenid
    }

    @ResponseBody
    @RequestMapping("/thanks", method = arrayOf(RequestMethod.GET))
    fun thankstoken(@RequestParam tokenid: String ): String {
        if (tokenid != null) {
            var ans = ""
            println("$tokenid")
            val slotnum = tokenid.get(0).toString()
            if (tokenArray[slotnum.toInt()].equals(tokenid)) {
                ans = createconnection("pickup", "pickup($tokenid)")
                tokenArray[slotnum.toInt()] = null
            }
            if (checkanswe(ans)) {
                return "ok"
            } else {
                tokenArray.set(tokenid.get(0).toString().toInt(), tokenid)
                return "error"
            }
        } else return "ok"
    }

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