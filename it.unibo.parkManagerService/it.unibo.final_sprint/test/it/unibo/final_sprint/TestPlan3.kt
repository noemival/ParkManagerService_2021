package it.unibo.final_sprint

import  org.junit.Assert.*;

import java.net.UnknownHostException
import org.junit.BeforeClass
import cli.System.IO.IOException
import org.junit.Test
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

class TestPlan3 {
	companion object{
		var parkservicestatusgui   : CoapObserver? = null
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
				myactor=QakContext.getActor("outsonar")
 				while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("outsonar")
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
	    println("+++++++++ BEEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver1=$parkservicestatusgui")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
		if( parkservicestatusgui == null)
			parkservicestatusgui = CoapObserver("parkservicestatusgui${counter++}","ctxparkingarea","parkservicestatusgui","8021")
		
	
  	}
	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${parkservicestatusgui!!.name}")
		parkservicestatusgui!!.terminate()
		parkservicestatusgui = null
		runBlocking{
			delay(1000)
		}
 	}
	@Test
    fun DTFREE() { 
		var result = ""
		runBlocking{

			val channelForObserver= Channel<String>()
			parkservicestatusgui!!.addObserver(channelForObserver,"alarm(a)")
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "outsonarocc", "outsonarocc(O)", "outsonar"), myactor!!)
						delay( 3000 )

			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester","takecar","takecar(T)","outsonar"),myactor!!)
			delay (1000)
			result = channelForObserver.receive()
		

			println("+++++++++result=$result") 
			  
			//delay( 500 ) 
 		    assertEquals( result, "alarm(a)")
		}
		
    }

}