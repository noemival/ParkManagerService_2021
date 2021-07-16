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
						println("fan in s0")
					}
					 transition(edgeName="t00",targetState="fanwork",cond=whenDispatch("fanstart"))
					transition(edgeName="t01",targetState="s0",cond=whenDispatch("fanstop"))
				}	 
				state("fanwork") { //this:State
					action { //it:State
						updateResourceRep("${currentState.stateName}" )

						println("fan: abbassando la temperatura")
						//updateResourceRep("fan working: lowering the temp" )
					}
					 transition(edgeName="t02",targetState="fanstop",cond=whenDispatch("fanstop"))
					transition(edgeName="t03",targetState="fanwork",cond=whenDispatch("fanstart"))
				}	 
				state("fanstop") { //this:State
					action { //it:State
						updateResourceRep("${currentState.stateName}" )

						println("fan: stoppato")
						//updateResourceRep("fan stopped" )

					}
					 transition(edgeName="t04",targetState="fanwork",cond=whenDispatch("fanstart"))
					transition(edgeName="t05",targetState="fanstop",cond=whenDispatch("fanstop"))
				}	 
			}
		}
}
