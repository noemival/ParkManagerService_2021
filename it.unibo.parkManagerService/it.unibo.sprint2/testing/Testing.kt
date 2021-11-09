
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Testing {
	companion object{
		var parkmanagerservice   : CoapObserver? = null
		var slotnum = ""
		var tokenId = ""
		var systemStarted         = false
		var channelSyncStart      = Channel<String>()

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
	    println("+++++++++ BEEFOREE testingObserver1=$parkmanagerservice")
		
		if( !systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
		if( parkmanagerservice == null)
			 parkmanagerservice = CoapObserver("obstesting${counter++}","ctxparkingarea","parkmanagerservice","8021")
	

				
  	}
	
	/*the following tests want to verify
	*if the robot will be in the correct position in the parkingarea after move request
	*/
//	@Test //the robot can start in every cell
	fun checkNotifyIn() {
		val strUrl = "http://localhost:8081/carenter"
		val client: HttpClient = HttpClientBuilder.create().build()
		val request = HttpGet(strUrl) 
		try{
			 var response: HttpResponse = client.execute(request)
	         var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
	         println("RESPONSE"  + "=$answer")
	         slotnum= answer
			 checkState("parkmanagerservice(acceptIn)")
		}catch(ex: Exception){
			println(""+ex)
		}
       
       
	}
//	@Test //the robot can start in every cell
	fun checkCarEnter() {
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
			 checkState("parkmanagerservice(handleCarEnter)")
		}catch(ex: Exception){
			println(""+ex)
		}
       
       
	}
	//@Test
	fun checkpickup() {
		val strUrl = "http://localhost:8081/thanks"
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
			 checkState("parkmanagerservice(acceptOut)")
		}catch(ex: Exception){
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

	@Test
	fun test(){
		checkNotifyIn()
		checkCarEnter()
		checkpickup()
	}
}
