/* Generated by AN DISI Unibo */ 
package it.unibo.outmanager

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Outmanager ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
			var start =  0L
				var difference = 0L
			
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("outmanager | start")
					}
					 transition(edgeName="t024",targetState="start",cond=whenEvent("outsonar"))
				}	 
				state("start") { //this:State
					action { //it:State
						 start = System.currentTimeMillis() 
						println("outmanager [start] | start Timer ) ")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						difference = System.currentTimeMillis() - start 
						println("outmanager [work] |  difference = $difference")
						if( difference >= 1000L 
						 ){println("outmanager [work]  | alarm event, time passed = $difference")
						emit("alarm", "alarm(a)" ) 
						}
						else
						 {forward("outfree", "outfree(occ)" ,"parkmanagerservice" ) 
						 println("outmanager [work]  | no alarm event ")
						 }
						stateTimer = TimerActor("timer_work", 
							scope, context!!, "local_tout_outmanager_work", 300.toLong() )
					}
					 transition(edgeName="t025",targetState="work",cond=whenTimeout("local_tout_outmanager_work"))   
					transition(edgeName="t026",targetState="free",cond=whenDispatch("takecar"))
				}	 
				state("free") { //this:State
					action { //it:State
						forward("outfree", "outfree(free)" ,"parkmanagerservice" ) 
						println("outmanager [free] | OUTDOORAREA FREE")
					}
				}	 
			}
		}
}