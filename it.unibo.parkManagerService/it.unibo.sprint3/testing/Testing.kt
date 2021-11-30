
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
import it.unibo.sprint_3.CoapObserver
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

//this testing file will use the ControllerPMSTester and it must comment @Controller annotation in the ControllerPms
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Testing {
	companion object{
		var parkmanagerservice   : CoapObserver? = null
		var weightsensorobserver   : CoapObserver? = null

		var slotnum = ""
		var tokenId = ""
		var systemStarted         = false
		var channelSyncStart      = Channel<String>()
		
		//lateinit var mqttconn : connQakMqttTest
		val client :  MqttClient =  MqttClient("tcp://broker.hivemq.com:1883", "testWeightsensor")

		//var testingObserver       : CoapObserverForTesting? = null
		var myactor               : ActorBasic? = null
		var counter               = 1
		@JvmStatic
        @BeforeClass
		//@Target([AnnotationTarget.FUNCTION]) annotation class BeforeClass
		//@Throws(InterruptedException::class, UnknownHostException::class, IOException::class)
		fun init() {
			GlobalScope.launch{
				it.unibo.ctxparkingarea.main()
				
			}
			GlobalScope.launch{				
				delay(2000)	//Give time to move lr
				channelSyncStart.send("starttesting")
			}
			
			
		}//init
		
	//	@JvmStatic
	  //  @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}	
	@Before
	fun checkSystemStarted()  {
		println("\n=================================================================== ") 
	    println("+++++++++ BEEFOREE testingObserver1=$parkmanagerservice && testingObserver2= $weightsensorobserver")
		
		if( !systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
				

			}			
		}
		if( parkmanagerservice == null){
			 parkmanagerservice = CoapObserver("obstesting${counter++}","ctxparkingarea","parkmanagerservice","8021")
		}
	if( weightsensorobserver == null){
			 weightsensorobserver = CoapObserver("obstesting${counter++}","ctxparkingarea","weightsensor","8021")
		}
				
  	}
	//@Test
	fun checkWeightSensor(){
		

		 runBlocking{
						delay(5000)
		 }
		publish("weightsensor/data","1000" )
		checkState("weightsensor(1000)", weightsensorobserver)
	}
	
	/*the following tests want to verify
	*if the robot will be in the correct position in the parkingarea after move request
	*/
	//@Test //the robot can start in every cell
	fun checkNotifyIn() {
		val strUrl = "http://localhost:8081/carenter"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpGet(strUrl) 
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
	         slotnum= answer
			
			 checkState("parkmanagerservice(acceptIn)",parkmanagerservice)
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
       
       
	}
	
fun checkCarEnter(expext : Int) {
		
		 runBlocking{
						delay(3000)
		 }
		val strUrl = "http://localhost:8081/tokenid"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpPost(strUrl)
		val params: MutableList<NameValuePair> = ArrayList()
        params.add(BasicNameValuePair("slotnum", slotnum))
		request.setEntity(UrlEncodedFormEntity(params))
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
	         tokenId= answer
			runBlocking{
			delay(1000)
			}
			if(expext == 1 ){
				checkState("parkmanagerservice(handleCarEnter)", parkmanagerservice)

			}else {
			 assertEquals("$answer","9")

			}
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
       
       
	}
	fun checkState(state: String, observer : CoapObserver? ){
		try {  
			runBlocking{
			var channelForObserver = Channel<String>()
			observer!!.addObserver(channelForObserver, state)
			var result = channelForObserver.receive()
			println("+++++++++result=$result and state=$state")
			assertEquals( result, state)
			}
			 
        } catch (ex: Exception) {
            println(ex.message)
        }
		
	} 

	@Test
	
	fun test(){
		
		connect()

		checkNotifyIn()//we want to simulate to get the slotnum-> it will be the same for the two different carenters
		
		checkWeightSensor() // we want to check the update of the resources in out application using mqtt

		checkCarEnter(1)// we want to simulate the carenter request from a client with slotnum=1
		
		checkCarEnter(2)// we want to simulate the carenter request from a client with slotnum=1 that couldn't be processed 
	}
	
	//MQTT CONNECTION
		fun connect(){
		val options = MqttConnectOptions()
		options.setKeepAliveInterval(480)
		options.setWill("unibo/clienterrors", "crashed".toByteArray(), 2, true)
		client.connect(options)
	}
	fun publish(topic : String, msg : String){
		val message = MqttMessage()
		message.setRetained(false)
		message.setPayload(msg.toByteArray())
		client.publish(topic, message)	
		
	}
}
