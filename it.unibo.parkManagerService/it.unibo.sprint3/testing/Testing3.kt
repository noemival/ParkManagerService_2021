
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
class Testing3 {
	companion object{
		var weightsensorobserver   : CoapObserver? = null
	
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
	    println("+++++++++ BEEFOREE testingObserver1=$weightsensorobserver")
		
		if( !systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
				

			}			
		} 
		if( weightsensorobserver == null){
			 weightsensorobserver = CoapObserver("obstesting${counter++}","ctxparkingarea","weightsensor","8021")
		}

				
  	}
	@Test
	fun checkWeightSensor(){
		
		connect()
		publish("weightsensor/data","1000" )
		checkState("weightsensor(1000)")
		
	}
	
	fun checkState(state: String,){
		try {  
			runBlocking{
			var channelForObserver = Channel<String>()
			weightsensorobserver!!.addObserver(channelForObserver, state)
			var result = channelForObserver.receive()
			println("+++++++++result=$result and state=$state")
			assertEquals( result, state)
			}
			 
        } catch (ex: Exception) {
            println(ex.message)
        }
		
}
	
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
