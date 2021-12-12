package utility

import JFlex.Options.time
import org.tigris.subversion.javahl.NodeKind.none
import kotlinx.coroutines.channels.Channel
import java.net.UnknownHostException
import cli.System.IO.IOException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.QakContext
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.junit.AfterClass
import it.unibo.kactor.sysUtil
import connQak.connQakTcp
import it.unibo.webBasicrobotqak.CoapSupport
import connQak.connQakCoap
import org.json.JSONObject


class HandleData{
		var trolleyObserver   : CoapObserver? = null
		
	
		
	public fun getState(actor : String): String{
			var result = ""
			val con = connQakCoap()
			con.createConnection()
			
			val  coapSupport = CoapSupport("coap://localhost:8021", "ctxparkingarea/$actor")
			result = coapSupport.readResource()
		println(result)
	
			return result
		}
	
	

}