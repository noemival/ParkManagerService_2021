package it.unibo.sprint1
import org.junit.After
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

import org.junit.Assert.*
import org.junit.Test
import org.junit.AfterClass

class Testing {
	companion object{
		var basicObserver   : CoapObserver? = null
		
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
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
	    println("+++++++++ BEEFOREE testingObserver1=$basicObserver")
		
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
		if( basicObserver == null)
			 basicObserver = CoapObserver("obstesting${counter++}","ctxparkingarea","basicrobot","8021")
	
	
  	}
	
	@Test //the robot start in cell 0,0
	fun testMoveToIn() {
		 try {
			 checkMove("lwwwwww","req(moveToIn)")
			
        } catch (ex: Exception) {
            println(ex.message)
        }
	}
	//@Test //the robot start in cell 0,0
	fun testMoveToSlotIn() {
		try {
			 checkMove("lwwwwrw","req(moveToSlotIn)")
			
        } catch (ex: Exception) {
            println(ex.message)
        }
	}
	//@Test //the robot start in cell 0,0
	fun testMoveToSlotOut() {
		try {
			 checkMove("lwwwwrww","req(moveToSlotOut)")

        } catch (ex: Exception) {
            println(ex.message)
        }
		
	}
	//@Test //the robot start in cell 0,0
	fun testMoveToOut() {
		 try {
			checkMove("wwwwlwwwwww","req(moveToOut)")

        } catch (ex: Exception) {
            println(ex.message)
        }
		
	}
	//@Test //the robot start in cell 0,0
	fun testMoveToHome() {
		 try {  
			val path1 = "lwwwwwwwwwww"
			var result = ""
			
			      
			sendMessage("req(moveToIn)")
			sendMessage("req(moveToHome)")
			runBlocking{
				delay(1000)   
				val channelForObserver1= Channel<String>()
				basicObserver!!.addObserver(channelForObserver1,"")
			
				for (i in 0 until path1.length+2){
						var r = channelForObserver1.receive()
						result = result + r
						println("+++++++++res=$result")
				}
				
				result=result.replace("ll", "")
				result=result.replace("rr", "")
				/*Comparison between realized and planned movements*/
				println("+++++++++result=$result and path=$path1")
				delay( 500 ) //give time to compensate before closing the test
	 		    assertEquals( result, path1) 
			}
			 
        } catch (ex: Exception) {
            println(ex.message)
        }
		
	}
	
	//@After
	fun backHome()  {
		println("+++++++++ AFTER  ${basicObserver!!.name}")
		basicObserver!!.terminate()
		basicObserver = null
		sendMessage("req(moveToHome)")
		runBlocking{
			delay(11000)
		}
		
		
 	}
	fun checkMove(pathPlaned : String, move : String){
		 try {  
			var path1 = pathPlaned
			var result = ""
			
			sendMessage(move)
			runBlocking{
				delay(1000)   
				var channelForObserver= Channel<String>()
				basicObserver!!.addObserver(channelForObserver,"")
			
				for (i in 0 until path1.length){
						var r = channelForObserver.receive()
						result = result + r
						println("+++++++++res=$result")
				}
				println("+++++++++result=$result and path=$path1")
				
				/*Comparison between realized and planned movements*/
				delay( 500 ) //give time to compensate before closing the test
	 		    assertEquals( result, path1)
			}
			 
        } catch (ex: Exception) {
            println(ex.message)
        }
		
	} 
	fun sendMessage(message : String){
        val strUrl = "http://localhost:8081/movetrolley"
        val client: HttpClient = HttpClientBuilder.create().build()
        val request = HttpPost(strUrl)

        val params: MutableList<NameValuePair> = ArrayList()
        params.add(BasicNameValuePair("move", message))
        //params.add(BasicNameValuePair("move", "trolleycmd(moveToSlotIn)"))
        request.setEntity(UrlEncodedFormEntity(params))
        var response: HttpResponse = client.execute(request)
        var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
       // println("RESPONSE"+ i++ +"=$answer")
        params.clear()
    }
	
}
