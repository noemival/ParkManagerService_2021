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

//this testing file will use the ControllerPMSTester and it must comment @Controller annotation in the ControllerPms
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Testing2 {
	companion object{
		
		var systemStarted         = false
		var channelSyncStart      = Channel<String>()
		
		val client :  MqttClient =  MqttClient("tcp://broker.hivemq.com:1883", "testFan")
		var myactor               : ActorBasic? = null
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
				myactor=QakContext.getActor("parkingmanager")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("parkingmanager")
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
/*	fun checkTempOver(){


		val strUrl = "http://localhost:8081/pm/test"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpGet(strUrl) 
		try{
			var response: HttpResponse = client.execute(request)
	        // var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	        // println("RESPONSE"  + "=$answer")
			
			val request = HttpGet(strUrl) 
		runBlocking(){
		
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "temperature", "temperature(40)","parkingmanager"), myactor!!)

			delay(8000)
		
			
		}
			 val request2 = HttpGet(strUrl)
			 var response : HttpResponse = client.execute(request2)
	         var answer = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
	runBlocking(){
		
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "temperature", "temperature(40)","parkingmanager"), myactor!!)

			delay(8000)
		
			
		}
			 val request3 = HttpGet(strUrl)
			 response = client.execute(request3)
	          answer = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
			assertEquals(answer,"trolley(stopped)fan(work)temp(40)")
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
	}
	*/
	fun checkFanStop(clientSocket : Socket){

		var response = ""
		var value = "msg(temp,event,mockthermometer,none,temp(20),20)"
		runBlocking(){
			delay(4000)
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "temperature", "temperature(30)","parkinmanager"), myactor!!)

			delay(500)
			
			val inFromServer = Scanner(clientSocket.getInputStream())
			response = inFromServer.nextLine();
			
			println(response)   
			println("Response= " +response)
			
			//assertEquals(response, "fan(off)")
			 
		}
		
    }
	@Test
	fun test(){
		runBlocking(){
			delay(4000)
			
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "temperature", "temperature(40)","parkingmanager"), myactor!!)

			delay(2000)
		}

	//checkTempOver()
		 
		}
}

