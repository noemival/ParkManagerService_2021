package it.unibo.sprint5

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
import utility.CoapObserver
import utility.HandleData


class TestPlan {
companion object{
		//var parkingmanagerObserver   : CoapObserver? = null
		var trolleyObserver   : CoapObserver? = null
		var systemStarted         = false
		val channelSyncStart      = Channel<String>()
		//var testingObserver       : CoapObserverForTesting? = null
		var myactor               : ActorBasic? = null
		var myactor2               : ActorBasic? = null

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
					myactor2=QakContext.getActor("trolley")
 				while(  myactor2 == null )		{
					println("+++++++++ waiting for system startup ...")
					delay(500)
					myactor2=QakContext.getActor("trolley")
				}
					
				
				delay(2000)	//Give time to move lr
				channelSyncStart.send("starttesting")
			
			}		 
		}//init
		
		/*Verify that the basicrobot has performed the movements defined by the planner to arrive at the goal */
		//@JvmStatic
	   // @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}//companion object

/*Before launching the testing, comment the thermometer qactor*/
	
	@Before
	fun checkSystemStarted()  {
		println("\n=================================================================== ") 
	    println("+++++++++ BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver1= $trolleyObserver")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
		/*if( parkingmanagerObserver == null)
			 parkingmanagerObserver = CoapObserver("parkingmanager","ctxparkingarea","parkingmanager","8022")
	*/
		if(trolleyObserver == null)
			trolleyObserver = CoapObserver("trolleyObserve","ctxparkingarea","trolley","8023")
		
}

	//comment parkmanagerservice before running the code
	@Test
    fun trolleyStop() {
    	runBlocking{
			val channelForObserver= Channel<String>()
			trolleyObserver!!.addObserver(channelForObserver,"trolley(stopped)")
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "trolleycmd", "trolleycmd(moveToIn)", "trolley"), myactor2!!)

				delay(3000)
		//val cmd2 = MsgUtil.buildEvent("tester", "temperature", "temperature(40)") 
			
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "temperature", "temperature(40)"), myactor!!)
			delay(1000)
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "stateChangetrolley", "stateChangetrolley(stop)"), myactor!!)
			//MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "trolleystop", "trolleystop(V)", "trolley"), myactor2!!)

			var result = channelForObserver.receive()
			//result= HandleData.getState("trolley")
			println("+++++++++result=$result")
			delay( 500 )  
 		    assertEquals( result, "trolley(stopped)")
		}
	}
		@Test
    fun trolleyResume() {
    	runBlocking{
			val channelForObserver= Channel<String>()
			trolleyObserver!!.addObserver(channelForObserver,"trolley(working)")
			MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "trolleycmd", "trolleycmd(moveToIn)", "trolley"), myactor2!!)

				delay(3000)
		//val cmd2 = MsgUtil.buildEvent("tester", "temperature", "temperature(40)") 
				MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "temperature", "temperature(40)"), myactor!!)
			delay(1000)
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "stateChangetrolley", "stateChangetrolley(stop)"), myactor!!)
			delay(3000)
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "temperature", "temperature(30)"), myactor!!)
			delay(1000)
			MsgUtil.sendMsg(MsgUtil.buildEvent("tester", "stateChangetrolley", "stateChangetrolley(work)"), myactor!!)
			//MsgUtil.sendMsg(MsgUtil.buildDispatch("tester", "trolleystop", "trolleystop(V)", "trolley"), myactor2!!)

			var result = channelForObserver.receive()
			//result= HandleData.getState("trolley")
			println("+++++++++result=$result")
			delay( 500 )  
 		    assertEquals( result, "trolley(working)")
		}
	}

	@After
	fun removeObs(){
		println("+++++++++ AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR  ${trolleyObserver!!.name}")
		trolleyObserver!!.terminate()
		trolleyObserver = null
		runBlocking{
			delay(1000)
		}
 	}
	


	
}