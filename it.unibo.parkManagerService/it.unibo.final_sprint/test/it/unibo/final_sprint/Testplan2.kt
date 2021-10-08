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
import Slotnum


class Testplan2 {
	companion object{
			
		var parkingservicegui   : CoapObserver? = null
		
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
		println("+++++++++ BEEFOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE testingObserver1=$parkingservicegui")
		if( ! systemStarted ) {
			runBlocking{
				channelSyncStart.receive()
				systemStarted = true
				   println("+++++++++ checkSystemStarted resumed ")
			}			
		} 
		if( parkingservicegui == null)
			parkingservicegui = CoapObserver("parkingserviceguitester","ctxparkingarea","parkingservicegui","8021")
			
	}	

	@Test
	fun checkSlotNum() {
		 for (i in 0..4){
            Slotnum.slotStateFree[i] = false
        }
		
		var result = ""
		runBlocking{

			val channelForObserver= Channel<String>()
			parkingservicegui!!.addObserver(channelForObserver,"slotNum(6)")
			result = channelForObserver.receive()
			println("+++++++++result=$result") 
 		    assertEquals( result, "slotNum(6)")
		}
		
    }
	
}		

