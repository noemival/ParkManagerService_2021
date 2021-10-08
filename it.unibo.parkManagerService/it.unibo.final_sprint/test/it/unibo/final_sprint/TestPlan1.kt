package it.unibo.final_sprint

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


class TestPlan1 {

companion object{
		
		var basicObserver   : CoapObserver? = null
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
		
		//@JvmStatic
	  //  @AfterClass
		fun terminate() {
			println("terminate the testing")
		}
		
	}//companion object


	@Before
	fun checkSystemStarted()  {
		println("\n=================================================================== ") 
	    println("+++++++++ BEEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver1=$basicObserver")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
			    println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
		if( basicObserver == null)
			 basicObserver = CoapObserver("basicrobot","ctxparkingarea","basicrobot","8021")
		
		if(trolleyObserver == null)
			trolleyObserver = CoapObserver("trolleyObserver","ctxparkingarea","trolley","8021")
		
		
		
  	}
	/*Before launching the testing, comment transition Goto working in parkmanagerService */
	@Test
	fun checkMoveRobot() {
		itunibo.planner.plannerUtil.initAI()
	    itunibo.planner.plannerUtil.loadRoomMap("parkingMap0") 
		itunibo.planner.plannerUtil.planForGoal("4","1")
		sysUtil.waitUser("PLEASE, put the robot at HOME",1000)
		val cmd = MsgUtil.buildDispatch("tester", "trolleycmd", "trolleycmd(moveToSlotIn)", "trolley") 
		var result = ""
		
		
		runBlocking{
			/*Add observer*/  
			val channelForObserver= Channel<String>()
			basicObserver!!.addObserver(channelForObserver,"")
			
			/*Send command to trolley actor and create the plan*/
			MsgUtil.sendMsg(cmd, myactor2!!)
			var path = itunibo.planner.plannerUtil.doPlan().toString()
			
			/*Delete special character from plan*/
			val re = Regex("[^A-Za-z0-9]")
			path=path.replace(re, "")
			path = path.replace(" ","")
			println("+++++++++path=$path for cmd=$cmd")
			
			/*Receive move execute by basicrobot*/
			for (i in 0 until path.length){
					var r = channelForObserver.receive()
					result = result + r	
			}
			println("+++++++++result=$result and path=$path for cmd=$cmd")
			
			/*Comparison between realized and planned movements*/
			delay( 500 ) //give time to compensate before closing the test
 		    assertEquals( result, path)

		}
	}
	
}

