package connQak

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
 

class connQakCoap() : connQakBase() {

lateinit var client   : CoapClient
	
	override fun createConnection(  ) {
		println("connQakCoap | createConnection hostIP=${robothostAddr} port=${robotPort}")
		val url = "coap://$robothostAddr:$robotPort/${ctxqakdest}/${qakdestination}"
		client = CoapClient(url)
		client.setTimeout(1000L)
		//initialCmd: to make console more reactive at the first user cmd
		val respGet = client.get() //CoapResponse
		if (respGet != null){
			println("connQakCoap | createConnection doing  get | CODE=  ${respGet.code}")
		println("connQakCoap | createConnection doing  get | RESPONSE=  ${respGet.responseText}")
	}
		else
				println("connQakCoap | url=  ${url} FAILURE")
	}
	
	override fun forward( msg: ApplMessage ){		
        val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        println("connQakCoap | PUT forward ${msg} RESPONSE CODE=  ${respPut.code}")		
	}
	
	override fun request( msg: ApplMessage ){
 		val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		if( respPut != null )
  		println("connQakCoap | answer= ${respPut.getResponseText()}")				
	}
	
	override fun emit( msg: ApplMessage){
		val url = "coap://$robothostAddr:$robotPort/$ctxqakdest"
		client = CoapClient( url )
        //println("PUT emit url=${url} ")		
         val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        println("connQakCoap | PUT emit ${msg} RESPONSE CODE=  ${respPut.code}")
		
	}	
}