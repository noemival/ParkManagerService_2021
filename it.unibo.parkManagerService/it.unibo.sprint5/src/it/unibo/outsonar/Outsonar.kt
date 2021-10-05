/* Generated by AN DISI Unibo */ 
package it.unibo.outsonar

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Outsonar ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("outsonar in s0")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
					}
					 transition(edgeName="t00",targetState="handlemsg",cond=whenDispatch("outsonarocc"))
				}	 
				state("handlemsg") { //this:State
					action { //it:State
						emit("outsonar", "outsonar(O)" ) 
						stateTimer = TimerActor("timer_handlemsg", 
							scope, context!!, "local_tout_outsonar_handlemsg", 100.toLong() )
					}
					 transition(edgeName="t01",targetState="handlemsg",cond=whenTimeout("local_tout_outsonar_handlemsg"))   
					transition(edgeName="t02",targetState="work",cond=whenDispatch("takecar"))
				}	 
			}
		}
}