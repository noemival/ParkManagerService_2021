/* Generated by AN DISI Unibo */ 
package it.unibo.parkingservicegui

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkingservicegui ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 	var SLOTNUM = 0  
				var RequestAccepted = 1
				var TOKENID = ""
				var fileName = "Tokenid.txt"
			    val file = java.io.File(fileName)
			    if(!file.exists()){
					file.createNewFile()
				}
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("parkingservicegui (client mock) |  start")
					}
					 transition( edgeName="goto",targetState="requestToEnter", cond=doswitch() )
				}	 
				state("requestToEnter") { //this:State
					action { //it:State
						println("parkingservicegui | requesttoenter ")
						request("notifyIn", "notifyIn(A)" ,"parkmanagerservice" )  
					}
					 transition(edgeName="t035",targetState="carEnter",cond=whenReply("informIn"))
				}	 
				state("carEnter") { //this:State
					action { //it:State
						println("parkingservicegUI | CARENTER ")
						delay(5000) 
						if( checkMsgContent( Term.createTerm("informIn(S)"), Term.createTerm("informIn(S)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var SLOTNUM= payloadArg(0).toInt()  
								println("parkingservicegui [carEnter] | receive SLOUTNUM = $SLOTNUM ")
								if( SLOTNUM >0 
								 ){delay(1000) 
								println("parkingservicegui | send CARENTER ")
								request("carenter", "carenter($SLOTNUM)" ,"parkmanagerservice" )  
								forward("weight", "weight(10000)" ,"weightsensor" ) 
								}
						}
					}
					 transition(edgeName="t036",targetState="saveTOKENID",cond=whenReply("receipt"))
				}	 
				state("saveTOKENID") { //this:State
					action { //it:State
						println("parkingservicegui | saveTOKENID")
						if( checkMsgContent( Term.createTerm("receipt(I)"), Term.createTerm("receipt(TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									TOKENID = payloadArg(0).toString()
								if( !TOKENID.equals("no") 
								 ){file.writeText(TOKENID)   
								println("parkingservicegui [saveTOKENID]: receive TOKENID = $TOKENID")
								}
								else
								 {println("parkingservicegui [saveTOKENID]: See you later")
								 }
						}
					}
					 transition( edgeName="goto",targetState="requestToEnter", cond=doswitchGuarded({TOKENID.equals("no") 
					}) )
					transition( edgeName="goto",targetState="wait", cond=doswitchGuarded({! (TOKENID.equals("no") 
					) }) )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("waiting")
						stateTimer = TimerActor("timer_wait", 
							scope, context!!, "local_tout_parkingservicegui_wait", 9000.toLong() )
					}
					 transition(edgeName="t037",targetState="requestPickup",cond=whenTimeout("local_tout_parkingservicegui_wait"))   
				}	 
				state("requestPickup") { //this:State
					action { //it:State
						println("parkingservicegui | requestPickup")
						request("pickup", "pickup($TOKENID)" ,"parkmanagerservice" )  
					}
					 transition(edgeName="t038",targetState="handlerResponsePickup",cond=whenReply("ok"))
				}	 
				state("handlerResponsePickup") { //this:State
					action { //it:State
						println("parkingservicegui| handlerResponsePickup")
						if( checkMsgContent( Term.createTerm("ok(O)"), Term.createTerm("ok(O)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								var  ok = payloadArg(0).toInt() 
								if( ok == 1 
								 ){println("parkingservicegui [handlerResponsePickup] | pickup request has been accepted and processed $ok")
								RequestAccepted = 1 
								forward("takecar", "takecar(P)" ,"outmanager" ) 
								forward("takecar", "takecar(P)" ,"outsonar" ) 
								}
								else
								 {RequestAccepted = 0 
								 println("parkingservicegui [handlerResponsePickup] |  wait for the outdoor area to be free")
								 delay(4000) 
								 }
						}
					}
					 transition( edgeName="goto",targetState="requestPickup", cond=doswitchGuarded({ RequestAccepted == 0 
					}) )
					transition( edgeName="goto",targetState="end", cond=doswitchGuarded({! ( RequestAccepted == 0 
					) }) )
				}	 
				state("end") { //this:State
					action { //it:State
						forward("end", "end(e)" ,"parkmanagerservice" ) 
					}
				}	 
			}
		}
}
