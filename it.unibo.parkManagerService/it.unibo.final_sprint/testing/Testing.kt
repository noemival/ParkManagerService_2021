
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Testing {
	companion object{
		var parkmanagerservice   : CoapObserver? = null
		var parkingmanagerobserver   : CoapObserver? = null


		var slotnum = ""
		var tokenId = ""
		var systemStarted         = false
		var channelSyncStart      = Channel<String>()
		
		//lateinit var mqttconn : connQakMqttTest
		val client :  MqttClient =  MqttClient("tcp://localhost:1883", "testWeightsensor")

		var myactor               : ActorBasic? = null
		var counter               = 1
		@JvmStatic
     	  	@BeforeClass
		fun init() {
			GlobalScope.launch{
				it.unibo.ctxparkingarea.main()
				
			}
			GlobalScope.launch{				
				delay(2000)	//Give time to move lr
				channelSyncStart.send("starttesting")
			}
			
			
		}
		fun terminate() {
			println("terminate the testing")
		}
		
	}	
	@Before
	fun checkSystemStarted()  {
		println("\n=================================================================== ") 
	    println("+++++++++ BEEFOREE testingObserver1=$parkmanagerservice && testingObserver3= $parkingmanagerobserver")
		
		if( !systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
				

			}			
		}
		if( parkmanagerservice == null){
			 parkmanagerservice = CoapObserver("obstesting${counter++}","ctxparkingarea","parkmanagerservice","8033")
		}
				
		if( parkingmanagerobserver == null){
			 parkingmanagerobserver = CoapObserver("obstesting${counter++}","ctxparkingarea","parkingmanager","8033")
		}
	for (i in 2..6) {
		Slotnum.uptadeSlotnum(i, false)
	}

  	}
	fun setWeightSensor(){
		var value = "msg(weight,event,tester,none,weight(41),16)"
		 runBlocking{
		delay(5000)
			 }
		publish("weightsensor/data",value )
		
	}
	
	
	fun checkState(state: String, observer : CoapObserver? ): Boolean{
		var result :String =""
		try {  
			runBlocking{
				var channelForObserver = Channel<String>()
				observer!!.addObserver(channelForObserver, state)
				result = channelForObserver.receive()
				println("+++++++++result=$result and state=$state")
				println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ ")
			}
        } catch (ex: Exception) {
            println(ex.message)
        }
			if(result.equals(state)){
					return true
				}else return false
			
	} 
	fun checkNotifyIn() {
		val strUrl = "http://localhost:8081/carentertest"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpGet(strUrl) 
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
	         slotnum= answer
			
			var acceptin= checkState("parkmanagerservice(acceptIn)", parkmanagerservice)
			var statepm= 	checkState("{\"statetrolley\":\"trolley(idle)\", \"statefan\":\"fan(stop)\", \"temp\":\"temp(0)\"}", parkingmanagerobserver)
			
			if( slotnum.toInt() >0 && acceptin && statepm){
				assertTrue(true)
			}else assertTrue(false)
 
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
       
       
	}
		fun checkNotifyIn2() {
		val strUrl = "http://localhost:8081/carentertest"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpGet(strUrl) 
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
	         slotnum= answer
			
			if( slotnum.toInt() ==9 ){
				assertTrue(true)
			}else assertTrue(false)
 
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
       
       
	}
	
fun checkCarEnter() {
		
		
		val strUrl = "http://localhost:8081/tokenidtest"
		println(strUrl)

		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpPost(strUrl)
	println(request)
		val params: MutableList<NameValuePair> = ArrayList()
        params.add(BasicNameValuePair("slotnum", slotnum))
		request.setEntity(UrlEncodedFormEntity(params))
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
	         tokenId= answer
			runBlocking{
			delay(2000)
			}
			

			var pms = checkState("parkmanagerservice(handleCarEnter)",parkmanagerservice)
			var statepm2= 	checkState("{\"statetrolley\":\"trolley(idle)\", \"statefan\":\"fan(stop)\", \"temp\":\"temp(0)\"}", parkingmanagerobserver)

			println(tokenId.get(0).toString())
		
			println(slotnum)
			 if(tokenId.get(0).toString().equals(slotnum) && pms && statepm2){
				
				assertTrue(true)
			}else assertTrue(false)
		}catch(ex: Exception){ 
			fail()
			println(""+ex)
		}
       
       
	}
	
		fun checkpickup() {
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
	         tokenId= answer
			
			var pms = checkState("parkmanagerservice(acceptOut)",parkmanagerservice)
			var statepm= checkState("{\"statetrolley\":\"trolley(idle)\", \"statefan\":\"fan(stop)\", \"temp\":\"temp(0)\"}", parkingmanagerobserver)
			if( tokenId.equals(answer) && pms && statepm){
				assertTrue(true)
			}else assertTrue(false)
			
		}catch(ex: Exception){
			fail()
			println(""+ex)
		}
       
       
	}
	
	
	
	
	
	@Test
	fun test(){
		/* The test will check the right operation of the parking and pickup phase of the system
		*  We will assume that the system is with 1 free slot, the temperature is costant, the fan is stopped and
		* the INDOORAREA is free. 
		* The request are sent with a intervall of two seconds. 
		* After the simulation we will simulate the enter request of another client tha should be not processed
		* because the INDOORAREA is still vacated and the slot aren't avaiable.
		*/
	
		connect()//we will connect with mqtt broker 
		checkNotifyIn()
		 runBlocking{
			delay(2000)
		 }
			setWeightSensor() // we should simulate the presence of the car
		runBlocking{
			delay(2000)
		 }
		checkCarEnter()
		runBlocking{
			delay(2000)
		 }
	
			checkpickup()
runBlocking{
			delay(2000)
		 }
		checkNotifyIn2() // the enter request of another client


		
	
	}
	
	//MQTT CONNECTION
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
}
