/* Generated by AN DISI Unibo */ 
package it.unibo.parkservicestatusgui

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkservicestatusgui ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("parkservicestatusgui | start")
					}
					 transition( edgeName="goto",targetState="working", cond=doswitch() )
				}	 
				state("working") { //this:State
					action { //it:State
						println("parkservicestatusgui | working")
					}
					 transition(edgeName="t029",targetState="handleAlarm",cond=whenDispatch("warning"))
				}	 
				state("handleAlarm") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("warning(V)"), Term.createTerm("warning(V)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var warning= payloadArg(0) 
								if(   warning == "SVEGLIA"   
								 ){emit("stateChangetrolley", "stateChangetrolley(stop)" ) 
								println("parkservicestatusgui  [handleAlarm]: send start command to fan and stop trolley ")
								}
								else
								 {emit("stateChangetrolley", "stateChangetrolley(work)" ) 
								 emit("stateChangefan", "stateChangefan(stop)" ) 
								 println("parkservicestatusgui  [handleAlarm]: send stop command to fan and resume trolley")
								 }
						}
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}