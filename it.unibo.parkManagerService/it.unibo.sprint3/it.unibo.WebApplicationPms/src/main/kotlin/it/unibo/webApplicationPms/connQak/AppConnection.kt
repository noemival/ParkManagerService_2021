package it.unibo.webApplicationPms.connQak

import it.unibo.kactor.MsgUtil

class AppConnection {

    var connToPms: connQakBase
    var answer = ""

    init {
        connToPms = connQakBase.create(ConnectionType.TCP)
        connToPms.createConnection()
    }
     fun sendRequest(messageId: String, content: String, qakDestination : String): String{
        val msg = MsgUtil.buildRequest(senderactor, messageId, content, qakDestination)
        answer = connToPms.requestWithRensponse(msg)

        return answer
    }
    fun sendEvent(messageId: String, content: String){
        val msg = MsgUtil.buildEvent(senderactor, messageId,content)
        connToPms.emit(msg)
    }
    fun sendForward(messageId: String, content: String, qakDestination : String){

        val msg = MsgUtil.buildDispatch(senderactor, messageId, content, qakDestination)
        connToPms.forward(msg)
    }
}