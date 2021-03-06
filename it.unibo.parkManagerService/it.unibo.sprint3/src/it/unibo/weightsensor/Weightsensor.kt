/* Generated by AN DISI Unibo */ 
package it.unibo.weightsensor

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Weightsensor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = true
						println("weightsensor | start")
					}
					 transition(edgeName="t05",targetState="work",cond=whenDispatch("weight"))
				}	 
				state("work") { //this:State
					action { //it:State
						delay(1000) 
						 var WEIGHT=100  
						println("weightsensor | work")
						if( checkMsgContent( Term.createTerm("weight(W)"), Term.createTerm("weight(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 WEIGHT = payloadArg(0).toInt()  
								println("weightsensor [work] | weight car: $WEIGHT")
								emit("weightsensor", "weightsensor($WEIGHT)" ) 
								println("weightsensor [work] | emit event")
						}
					}
				}	 
			}
		}
}
