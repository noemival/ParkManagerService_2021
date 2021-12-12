package it.unibo.webApplicationPms.controller


import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.webApplicationPms.utility.Button
import it.unibo.webApplicationPms.connQak.*
import it.unibo.webApplicationPms.email.EmailServiceImp
import it.unibo.webApplicationPms.utility.Slotnum
import it.unibo.webApplicationPms.utility.Tokenid
import it.unibo.webApplicationPms.utility.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


/* For launching test comment annotation @Controller in ControllerPms
and decomment annotation @Controller in Controller in ControllerPmsTester */
@kotlinx.coroutines.ObsoleteCoroutinesApi
//@Controller
class ControllerPmsTest (private val emailServiceImp: EmailServiceImp){

    lateinit var connToPms: connQakBase
    var answer = ""
    var tokenArray = arrayOfNulls<String>(7)
    var appConnection : AppConnection = AppConnection()

    @ResponseBody
    @RequestMapping("/carenter", method = arrayOf(RequestMethod.GET))
    fun carEnter(button: Button, slotnum: Slotnum): String {
        var slot = getSlotNum()

        if (slot.equals("0")) {
            button.enterdisabled = false

        } else {
            if(slot.equals("9")){
                return "error.html"
            }else{
                button.enterdisabled = true
                slotnum.slotnum = slot.toInt()
                println(slotnum.slotnum)
            }

        }
        return slotnum.slotnum.toString()
    }

    @ResponseBody
    @RequestMapping("/tokenid", method = arrayOf(RequestMethod.POST))
    fun getTokenId(@RequestParam slotnum : Int): String {
        var ans = ""
        println(slotnum)

        var tokenid : Tokenid = Tokenid()
        if (slotnum != 0) {
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
    @RequestMapping("/thanks", method = arrayOf(RequestMethod.GET))
    fun thanksToken(@RequestParam tokenid: String ): String {
        if (tokenid != null) {
            var ans = ""
            println("$tokenid")
            val slotnum = tokenid.get(0).toString()
            if (tokenArray[slotnum.toInt()].equals(tokenid)) {
                ans = appConnection.sendRequest("pickup","pickup($tokenid)","parkmanagerservice")
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
        answer= appConnection.sendRequest("notifyIn","notifyIn(A)","parkmanagerservice")
        var slotNum = answer.get(71)
        return slotNum.toString()
    }

  /*  fun createconnection(messageId: String, content: String): String{
        connToPms = connQakBase.create(ConnectionType.TCP)
        connToPms.createConnection()
        val msg = MsgUtil.buildRequest("parkmanagerserviceProxy", messageId, content, qakdestination)
        answer = connToPms.requestWithRensponse(msg)
        print(answer)
        return answer
    }*/
    fun checkanswe(ans: String):Boolean{

        var answer= ans.get(59).toString()
        var intans=answer.toInt()
        if(intans==1){
            print(answer)
            return true
        }else return false
    }
}