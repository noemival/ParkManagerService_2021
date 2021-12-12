package it.unibo.final_sprint
import org.eclipse.paho.client.mqttv3.*;
import org.junit.After
import org.junit.runners.MethodSorters;
import org.apache.commons.io.IOUtils
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import java.lang.Exception
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.BasicNameValuePair
import org.apache.http.NameValuePair
import java.util.ArrayList
import org.junit.Assert.*
import java.net.UnknownHostException
import org.junit.BeforeClass
import cli.System.IO.IOException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import it.unibo.kactor.QakContext
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ApplMessage
import org.junit.Before
import org.junit.FixMethodOrder;
import org.junit.Assert.*
import org.junit.Test
import org.junit.AfterClass
import org.apache.http.client.methods.HttpGet
import org.apache.http.nio.client.HttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import java.util.concurrent.Future
import org.apache.http.client.utils.URIBuilder
import org.apache.http.client.methods.HttpRequestBase
import java.net.Socket
import java.util.Scanner
import java.util.UUID
import okio.Utf8

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Testing2 {
	companion object{
		
		var systemStarted         = false
		var channelSyncStart      = Channel<String>()
		
		//val client :  MqttClient =  MqttClient("tcp://broker.hivemq.com:1883", "testFan")
		var parkingmanager               : ActorBasic? = null
		var outmanager               : ActorBasic? = null

		var counter               = 1
		//var testingObserver       : CoapObserverForTesting? = null
		
		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
        fun init() {
			GlobalScope.launch{
				it.unibo.ctxparkingarea.main() //keep the control
			}
			GlobalScope.launch{
				parkingmanager=QakContext.getActor("parkingmanager")
 				while(  parkingmanager == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					parkingmanager=QakContext.getActor("parkingmanager")
				}
					outmanager=QakContext.getActor("outmanager")
 				while(  outmanager == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					outmanager=QakContext.getActor("outmanager")
				}				
				delay(5000)	//Give time to move lr
				channelSyncStart.send("starttesting")
			}
	

		}
	//	@JvmStatic
	  //  @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}	
	
	//@Test
	fun conn(state: String, url:String):String{
			var answer = ""
			runBlocking(){
			delay(4000)
			}
		val strUrl = "http://localhost:8081/pm/$url"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpGet(strUrl) 
		try{
		
				//sendDisp("temperature","temperature(40)")

	
			var response: HttpResponse = client.execute(request)
	        answer= IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	        println("RESPONSE"  + "=$answer")
			println(state)
			
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
		return answer
	}
	fun	sendDisp(msgId: String, content: String){
			runBlocking(){
			delay(2000)
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", msgId, content,"parkingmanager"), parkingmanager!!)
		}
    }
		@Test
		/*
		* We want check the correct the correct information sending to the GUI when the OUTDOORAREA is still vacated over a DTFREE time. 
		*/
	fun test1(){

		runBlocking(){
			delay(2000)
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "outsonar", "outsonar(V)"), outmanager!!)
			delay(1000)
		}
			
		assertEquals("alarm(occ)",conn("alarm(occ)","alarm"))
		runBlocking(){
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "takecar", "takecar(V)","outmanager"), outmanager!!)
			delay(1000)
		}
		
		}
	@Test
	/*
		* We want check the correct operation of manage and monitor, so we should check if the GUI will received the right informatio of the parking area state 
		* in the followiong simulated scene: when the temperature overcome/undercome tmax 
		* and when the button is pressed by the manager
		*/
	fun test2(){
		
		
		var answer=""
		runBlocking(){
			delay(2000)
		}
		conn("trolley(idle)fan(stop)temp(40)","test")
		sendDisp("temperature","temperature(40)")
		runBlocking(){
			delay(2000)
		}
		assertEquals("trolley(idle)fan(stop)temp(40)",conn("trolley(idle)fan(stop)temp(40)","test") )
		sendDisp("stateChange", "stateChange(over)")
		
		runBlocking(){
			delay(2000)
		}
		sendDisp("temperature","temperature(40)")
				assertEquals("trolley(stopped)fan(work)temp(40)",conn("trolley(stopped)fan(work)temp(40)","test") )
		
		sendDisp("temperature","temperature(30)")
	assertEquals("trolley(stopped)fan(work)temp(30)",conn("trolley(stopped)fan(work)temp(30)","test"))
		runBlocking(){
			delay(2000)
		}
		sendDisp("stateChange", "stateChange(under)")
		assertEquals("trolley(stopped)fan(work)temp(30)",conn("trolley(stopped)fan(work)temp(30)","test"))
		sendDisp("temperature","temperature(30)")
		runBlocking(){
			delay(2000)
		}
		assertEquals("trolley(idle)fan(stop)temp(30)",conn("trolley(idle)fan(stop)temp(30)","test"))

		
		}

}

