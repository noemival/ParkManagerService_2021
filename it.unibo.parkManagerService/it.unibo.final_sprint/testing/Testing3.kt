
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
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import it.unibo.final_sprint.CoapObserver

class Testing3{	
	companion object{
		var parkmanagerservice   : CoapObserver? = null
		//var weightsensorobserver   : CoapObserver? = null
		var parkingmanagerobserver   : CoapObserver? = null

		
		var slotnum = ""
		var tokenId = ""
		var systemStarted         = false
		var channelSyncStart      = Channel<String>()
		
		//lateinit var mqttconn : connQakMqttTest
		val client :  MqttClient =  MqttClient("tcp://localhost:1883", "testr")

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
				myactor=QakContext.getActor("outmanager")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("outmanager")
				}				
				delay(5000)	//Give time to move lr
				channelSyncStart.send("starttesting")
			}		 
			
			
		}//init
		
	//	@JvmStatic
	  //  @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
	}
		//@Test
		fun checkNotifyIn() {
		val strUrl = "http://localhost:8081/carentertest"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpGet(strUrl) 
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE slotnum"  + "=$answer")
	         slotnum= answer
			 checkState("parkmanagerservice(acceptIn)")
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
       
       
	}
//	@Test //the robot can start in every cell
	fun checkCarEnter() {
		val strUrl = "http://localhost:8081/tokenidtest"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpPost(strUrl)
		val params: MutableList<NameValuePair> = ArrayList()
        params.add(BasicNameValuePair("slotnum", slotnum))
		request.setEntity(UrlEncodedFormEntity(params))
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE token"  + "=$answer")
	         tokenId= answer
			 checkState("parkmanagerservice(handleCarEnter)")
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
       
       
	}
	//@Test
	fun pickupRefused() {
		runBlocking(){
			delay(4000)
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "outsonar", "outsonar(10)","outmanager"), myactor!!)
			delay(8000)
		}
		
		val strUrl = "http://localhost:8081/thanktest"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpGet(strUrl)
		val params: MutableList<NameValuePair> = ArrayList()
        params.add(BasicNameValuePair("tokenid", tokenId))
		val uri = URIBuilder(request.getURI()).addParameters(params).build()
		request.setURI(uri)
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
	         var resp= answer
			 assertEquals("error", resp)
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
	}

	fun checkState(state: String,){
		try {  
			runBlocking{
			delay(1000)   
			var channelForObserver = Channel<String>()
			parkmanagerservice!!.addObserver(channelForObserver, state)
			var result = channelForObserver.receive()
			println("+++++++++result=$result and state=$state")
			assertEquals( result, state)
			} 
			 
        } catch (ex: Exception) {
            println(ex.message)
        }
	}
	fun setWeightSensor(){
		var value = "msg(weight,event,tester,none,weight(41),16)"
	
		publish("weightsensor/data",value )
		//checkState("weightsensor(1000)", weightsensorobserver)
		 
	}
	fun connect(){
		val options = MqttConnectOptions()
		options.setKeepAliveInterval(480)
		options.setWill("unibo/clienterrors", "crashed".toByteArray(), 2, true)
		println("connesso")
		client.connect(options)
	}
	fun publish(topic : String, msg : String){
		val message = MqttMessage()
		message.setRetained(false)
		message.setPayload(msg.toByteArray())
		client.publish(topic, message)	
		
	}
	
	
	@Test
	fun test(){
		connect()
		checkNotifyIn()
		setWeightSensor()
		checkCarEnter()
		pickupRefused()
	}
}		
