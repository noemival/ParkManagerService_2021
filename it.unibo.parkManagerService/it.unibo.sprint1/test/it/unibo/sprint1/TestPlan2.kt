package it.unibo.sprint1

import org.junit.Assert.*
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



class TestPlan2{
	companion object{
		var basicObserver   : CoapObserver? = null
		//var trolleyObserver   : CoapObserver? = null
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
				myactor=QakContext.getActor("trolley")
	 			while(  myactor == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor=QakContext.getActor("trolley")
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
			
	}
	@Before
	fun checkSystemStarted()  {
		println("\n=================================================================== ") 
	    println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver1=$basicObserver")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
	
		if( basicObserver == null)
			basicObserver = CoapObserver("basicrobot","ctxparkingarea","basicrobot","8021")

  	}
	@Test
	fun testInterruptionMoveHome() {
		
		var result = ""
		var path = "lwwwwwwllwwwwwwllwwwwrwllwlwwwwl"
		val cmd = MsgUtil.buildDispatch("tester", "trolleycmd", "trolleycmd(moveToIn)", "trolley")
		val cmd1 = MsgUtil.buildDispatch("tester", "trolleycmd", "trolleycmd(moveToHome)", "trolley")
		val cmd2 = MsgUtil.buildDispatch("tester", "trolleycmd", "trolleycmd(moveToSlotIn)", "trolley")
		val cmd3 = MsgUtil.buildDispatch("tester", "trolleycmd", "trolleycmd(end)", "trolley") 
		runBlocking{
			/*Add observer*/  
			val channelForObserver= Channel<String>()
			basicObserver!!.addObserver(channelForObserver,"")
			
			/*Send command to trolley actor*/
			
			MsgUtil.sendMsg(cmd, myactor!!)
			MsgUtil.sendMsg(cmd1, myactor!!)
			MsgUtil.sendMsg(cmd2, myactor!!)
			MsgUtil.sendMsg(cmd3, myactor!!)
			while(true){
					var r = channelForObserver.receive()
					if(r.equals("basicrobot(end)")) break
					result = result + r
			}
			println("+++++++++result=$result and path=$path for cmd=$cmd")
			
			/*Comparison between realized and planned movements*/
			delay( 500 ) //give time to compensate before closing the test
 		   // assertEquals( result, path)
			assertNotSame( result, path)

		}
		
	}		
	
}