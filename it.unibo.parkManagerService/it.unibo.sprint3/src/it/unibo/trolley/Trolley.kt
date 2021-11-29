/* Generated by AN DISI Unibo */ 
package it.unibo.trolley

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Trolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		  val mapname     = "parkingMap0"  		  
		  var Myself      = myself     
		  var CurrentPlannedMove = ""   
		  var MOVE = ""
		  var terminate =  0
		  var home = 0
		  var counter = 0
		  var trolleyCmd = ""
		  var listCommand = arrayListOf<String>()
		  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("trolley | start ")
						TrolleyPlannerSupport.initPlanner("$mapname") 
					}
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						println("trolley [idle]  | waiting......... ")
					}
					 transition(edgeName="t120",targetState="working",cond=whenDispatch("trolleycmd"))
				}	 
				state("working") { //this:State
					action { //it:State
						println("trolley | working")
						home = 0 
						if( checkMsgContent( Term.createTerm("trolleycmd(MOVETO)"), Term.createTerm("trolleycmd(V)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									trolleyCmd = "${payloadArg(0)}" 	 
												TrolleyPlannerSupport.setGoal(trolleyCmd)
												if(trolleyCmd == "moveToHome"){
													home =  1
												}
												if(trolleyCmd == "end"){
													terminate =  1
												}
						}
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("execPlannedMoves") { //this:State
					action { //it:State
						delay(400) 
						CurrentPlannedMove = TrolleyPlannerSupport.getNextMove()  
					}
					 transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({ CurrentPlannedMove.length>0   
					}) )
					transition( edgeName="goto",targetState="finishPlannedMoves", cond=doswitchGuarded({! ( CurrentPlannedMove.length>0   
					) }) )
				}	 
				state("doMove") { //this:State
					action { //it:State
						forward("cmd", "cmd($CurrentPlannedMove)" ,"basicrobot" ) 
						stateTimer = TimerActor("timer_doMove", 
							scope, context!!, "local_tout_trolley_doMove", 100.toLong() )
					}
					 transition(edgeName="t121",targetState="execPlannedMoves",cond=whenTimeout("local_tout_trolley_doMove"))   
					transition(edgeName="t122",targetState="working",cond=whenDispatchGuarded("trolleycmd",{home == 1 && terminate == 0 
					}))
				}	 
				state("finishPlannedMoves") { //this:State
					action { //it:State
						println("trolley | finishPlannedMoves")
						if(home == 1 || terminate == 1){
						 		
						 			listCommand =  TrolleyPlannerSupport.atHome()
									for (command in listCommand) {   
						   				forward("cmd", "cmd(${command})" ,"basicrobot" )
									}
								}else{ 
									listCommand =  TrolleyPlannerSupport.loadUnloadCar()
									for (command in listCommand) {
						   				forward("cmd", "cmd(${command})" ,"basicrobot" )
									}
										delay(1000)//we want to simulate load/unload the car
								} 
						updateResourceRep(TrolleyPlannerSupport.getPosition() 
						)
					}
					 transition( edgeName="goto",targetState="endWork", cond=doswitchGuarded({ terminate == 1   
					}) )
					transition( edgeName="goto",targetState="idle", cond=doswitchGuarded({! ( terminate == 1   
					) }) )
				}	 
				state("endWork") { //this:State
					action { //it:State
						println("trolley |  endWork")
						forward("end", "end(V)" ,"basicrobot" ) 
					}
				}	 
			}
		}
}
