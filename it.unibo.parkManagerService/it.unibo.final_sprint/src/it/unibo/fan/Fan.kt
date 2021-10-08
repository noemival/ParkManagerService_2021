/* Generated by AN DISI Unibo */ 
package it.unibo.fan

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Fan ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("fan | start")
					}
					 transition( edgeName="goto",targetState="stopped", cond=doswitch() )
				}	 
				state("stopped") { //this:State
					action { //it:State
						println("fan | stopped")
						updateResourceRep("fan(stop)" 
						)
					}
					 transition(edgeName="t010",targetState="working",cond=whenDispatch("fanstart"))
				}	 
				state("working") { //this:State
					action { //it:State
						println("fan | working")
						updateResourceRep("fan(work)" 
						)
					}
					 transition(edgeName="t011",targetState="stopped",cond=whenDispatch("fanstop"))
				}	 
			}
		}
}
