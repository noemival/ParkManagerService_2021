package it.unibo.final_sprint

import org.junit.Assert.*
import org.junit.Test
import java.net.UnknownHostException
import org.junit.BeforeClass
import cli.System.IO.IOException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import it.unibo.kactor.QakContext
import org.junit.Before
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.junit.AfterClass
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ApplMessage
import org.junit.After

class Testplan4 {
		companion object{
		var fanObserver   : CoapObserver? = null
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
				it.unibo.ctxparkingarea.main() //keep the control
			}
			GlobalScope.launch{
				myactor=QakContext.getActor("parkingmanager")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("parkingmanager")
				}				
				delay(2000)	//Give time to move lr
				channelSyncStart.send("starttesting")
			}		 
		}//init
		
		//@JvmStatic
	   // @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}//companion object

	
	@Before
	fun checkSystemStarted()  {
		println("\n=================================================================== ") 
	    println("+++++++++ BEEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver1=$fanObserver")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
		if( fanObserver == null) fanObserver = CoapObserver("fan${counter++}","ctxparkingarea","fan","8021")
		
	
  	}
	
	//Before launching the testing, comment thermometer qactor
	@Test
    fun fanStop() { 
		var result = ""
		runBlocking{

			val channelForObserver= Channel<String>()
			fanObserver!!.addObserver(channelForObserver,"fan(stop)")
			delay(5000)
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "temperature", "temperature(40)"), myactor!!)
			delay(5000)
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "temperature", "temperature(30)"), myactor!!)
			
			
			result = channelForObserver.receive()
		

			println("+++++++++result=$result") 
			  
			delay( 500 ) 
 		    assertEquals( result, "fan(stop)")
		}
		
    }
    @Test
	
    fun fanWork() {
    	runBlocking{
			val channelForObserver= Channel<String>()
			fanObserver!!.addObserver(channelForObserver,"fan(work)")
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "temperature", "temperature(40)"), myactor!!)
			var result = channelForObserver.receive()
			println("+++++++++result=$result")
			delay( 500 )  
 		    assertEquals( result, "fan(work)")
		}
	 }
 
	
}
