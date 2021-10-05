/* Generated by AN DISI Unibo */ 
package it.unibo.automaticfan

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Automaticfan ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("automaticfan | start")
					}
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("waiting") { //this:State
					action { //it:State
					}
					 transition(edgeName="t020",targetState="fanstart",cond=whenDispatch("fanstart"))
					transition(edgeName="t021",targetState="fanStop",cond=whenDispatch("fanstop"))
				}	 
				state("fanstart") { //this:State
					action { //it:State
						println("automaticfan | fanstart ")
						forward("fanstart", "fanstart(V)" ,"fan" ) 
					}
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("fanStop") { //this:State
					action { //it:State
						println("automaticfan | fan stopped ")
						forward("fanstop", "fanstop(off)" ,"fan" ) 
					}
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
			}
		}
}
