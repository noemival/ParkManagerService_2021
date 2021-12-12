package it.unibo.webApplicationPms.connQak

import com.andreapivetta.kolor.Color
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.sysUtil

class AppConnection {

    lateinit var connToPms: connQakBase
    var answer = ""

     fun sendRequest(messageId: String, content: String, qakDestination : String): String{

         connToPms = connQakBase.create(ConnectionType.TCP)
         connToPms.createConnection()

         val msg = MsgUtil.buildRequest(senderactor, messageId, content, qakDestination)
        answer = connToPms.requestWithRensponse(msg)

        return answer
    }
    fun sendEvent(messageId: String, content: String){

        connToPms = connQakBase.create(ConnectionType.TCP)
        connToPms.createConnection()

        val msg = MsgUtil.buildEvent(senderactor, messageId,content)
        connToPms.emit(msg)
        print(msg)
    }
    fun sendForward(messageId: String, content: String, qakDestination : String){

        connToPms = connQakBase.create(ConnectionType.TCP)
        connToPms.createConnection()

        val msg = MsgUtil.buildDispatch(senderactor, messageId, content, qakDestination)
        connToPms.forward(msg)
    }
}