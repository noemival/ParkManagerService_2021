/* Generated by AN DISI Unibo */ 
package it.unibo.parkmanagerservice

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkmanagerservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 	var SLOTNUM = 0
				var CARSLOTNUM = 0 
				var TOKENID = " "
				var INFREE =1
				var OUTFREE = 1
				var stateTrolley =1
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("parkmanagerservice | start")
					}
					 transition( edgeName="goto",targetState="working", cond=doswitch() )
				}	 
				state("working") { //this:State
					action { //it:State
						println("parkmanagerservice | working")
					}
					 transition(edgeName="t09",targetState="acceptIn",cond=whenRequest("notifyIn"))
					transition(edgeName="t010",targetState="acceptOut",cond=whenRequest("pickup"))
					transition(edgeName="t011",targetState="handleCarenter",cond=whenRequest("carenter"))
				}	 
				state("acceptIn") { //this:State
					action { //it:State
						println("parkManagerService | acceptIn")
						if( checkMsgContent( Term.createTerm("notifyIn(N)"), Term.createTerm("notifyIn(N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  INFREE ==1 && stateTrolley ==1 
								 ){ SLOTNUM = Slotnum.getSlotnum()  
								answer("notifyIn", "informIn", "informIn($SLOTNUM)"   )  
								Slotnum.uptadeSlotnum(SLOTNUM, false) 
								}
								else
								 {println("parkManagerService [acceptIn] | indoor-area occupied, the request could not be processed")
								 }
						}
					}
					 transition(edgeName="t012",targetState="handleCarenter",cond=whenRequest("carenter"))
					transition(edgeName="t013",targetState="acceptOut",cond=whenRequest("pickup"))
					transition(edgeName="t014",targetState="acceptIn",cond=whenRequest("notifyIn"))
				}	 
				state("handleCarenter") { //this:State
					action { //it:State
						println("parkManagerService | caraenter")
						if( checkMsgContent( Term.createTerm("carenter(C)"), Term.createTerm("carenter(S)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  INFREE=0 
												SLOTNUM= payloadArg(0).toInt()
								if(  SLOTNUM!= 0  
								 ){println("parkManagerService [carenter] | send to trolley moveToIn")
								forward("trolleycmd", "trolleycmd(moveToIn)" ,"trolley" ) 
								delay(200) 
								INFREE = 1 
								if( INFREE == 1 
								 ){ 
														var MOVETOSLOT = "moveToSlot".plus(SLOTNUM)
								println("parkManagerService [carenter] | send to trolley $MOVETOSLOT")
								forward("trolleycmd", "trolleycmd($MOVETOSLOT)" ,"trolley" ) 
								 TOKENID=  Slotnum.generateTOKENID(SLOTNUM)  
								println("parkManagerService [carenter] | send to client TOOKENID = $TOKENID")
								answer("carenter", "receipt", "receipt($TOKENID)"   )  
								}
								}
								else
								 {forward("trolleycmd", "trolleycmd(moveToHome)" ,"trolley" ) 
								 }
						}
					}
					 transition( edgeName="goto",targetState="working", cond=doswitch() )
				}	 
				state("acceptOut") { //this:State
					action { //it:State
						println("parkManagerService | acceptOut ")
						if( OUTFREE == 1 && stateTrolley == 1 
						 ){if( checkMsgContent( Term.createTerm("pickup(TOKENID)"), Term.createTerm("pickup(TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								TOKENID= payloadArg(0).toString()  
								println("parkManagerService [acceptOut] | receive TOKENID = $TOKENID")
								 CARSLOTNUM = Slotnum.findSlot(TOKENID)  
								println("parkManagerService [acceptOut]]: receive CARSLOTNUM = $CARSLOTNUM")
								answer("pickup", "ok", "ok($OUTFREE)"   )  
								
													Slotnum.uptadeSlotnum(CARSLOTNUM, true)
													var MOVETOSLOT = "moveToSlot".plus(CARSLOTNUM)
								forward("trolleycmd", "trolleycmd($MOVETOSLOT)" ,"trolley" ) 
								forward("trolleycmd", "trolleycmd(moveToOut)" ,"trolley" ) 
						}
						}
						else
						 {answer("pickup", "ok", "ok($OUTFREE)"   )  
						 println("parkManagerService [acceptOut]: outdoor-area occupied, the request could not be processed")
						 }
					}
					 transition( edgeName="goto",targetState="working", cond=doswitch() )
				}	 
			}
		}
}