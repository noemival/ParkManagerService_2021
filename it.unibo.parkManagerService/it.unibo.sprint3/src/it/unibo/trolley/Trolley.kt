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
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("trolley | start")
						itunibo.planner.plannerUtil.initAI(  )
						println("&&&  trolley loads the parking map from the given file ...")
						itunibo.planner.plannerUtil.loadRoomMap( "$mapname"  )
						itunibo.planner.plannerUtil.showMap(  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
					}
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						println("trolley [idle]  | waiting......... ")
					}
					 transition(edgeName="t121",targetState="working",cond=whenDispatch("trolleycmd"))
				}	 
				state("working") { //this:State
					action { //it:State
						home = 0 
						println("trolley | working")
						if( checkMsgContent( Term.createTerm("trolleycmd(MOVETO)"), Term.createTerm("trolleycmd(MOVE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 MOVE =  "${payloadArg(0).toString()}"  
								println("trolley [working] | Current move: $MOVE")
								if(  MOVE == "moveToIn"  
								 ){println("trolley [working] | moveToIn ")
								itunibo.planner.plannerUtil.planForGoal( "6", "0"  )
								}
								if(  MOVE == "moveToSlot1"  
								 ){println("trolley [working] | moveToSlot1 ")
								itunibo.planner.plannerUtil.planForGoal( "4", "1"  )
								}
								if(  MOVE == "moveToSlot2"  
								 ){println("trolley [working] | moveToSlot2 ")
								itunibo.planner.plannerUtil.planForGoal( "1", "1"  )
								}
								if(  MOVE == "moveToSlot3"  
								 ){println("trolley [working] | moveToSlot3 ")
								itunibo.planner.plannerUtil.planForGoal( "4", "2"  )
								}
								if(  MOVE == "moveToSlot4"  
								 ){println("trolley [working] | moveToSlot4 ")
								itunibo.planner.plannerUtil.planForGoal( "1", "2"  )
								}
								if(  MOVE == "moveToSlot5"  
								 ){println("trolley [working] | moveToSlot5 ")
								itunibo.planner.plannerUtil.planForGoal( "4", "3"  )
								}
								if(  MOVE == "moveToSlot6"  
								 ){println("trolley [working] | moveToSlot6 ")
								itunibo.planner.plannerUtil.planForGoal( "1", "3"  )
								}
								if(  MOVE == "moveToOut"  
								 ){println("trolley [working] | moveToOut ")
								itunibo.planner.plannerUtil.planForGoal( "6", "4"  )
								}
								if( MOVE  == "moveToHome" 
								 ){println("trolley [working] | $MOVE ")
								itunibo.planner.plannerUtil.planForGoal( "0", "0"  )
								 home =  1 
								}
								if(  MOVE == "end" 
								 ){println("trolley [working] | $MOVE ")
								itunibo.planner.plannerUtil.planForGoal( "0", "0"  )
								 terminate = 1 
								}
						}
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("execPlannedMoves") { //this:State
					action { //it:State
						delay(400) 
						  CurrentPlannedMove = itunibo.planner.plannerUtil.getNextPlannedMove()  
						println("trolley [execPlannedMoves] | execPlannedMoves = $CurrentPlannedMove")
					}
					 transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({ CurrentPlannedMove.length>0   
					}) )
					transition( edgeName="goto",targetState="finishPlannedMoves", cond=doswitchGuarded({! ( CurrentPlannedMove.length>0   
					) }) )
				}	 
				state("doMove") { //this:State
					action { //it:State
						if(  CurrentPlannedMove == "l" 
						 ){}
						if( CurrentPlannedMove == "r"   
						 ){}
						if( CurrentPlannedMove == "w"   
						 ){}
						println("trolley | doMove")
						itunibo.planner.plannerUtil.updateMap( "$CurrentPlannedMove"  )
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						stateTimer = TimerActor("timer_doMove", 
							scope, context!!, "local_tout_trolley_doMove", 100.toLong() )
					}
					 transition(edgeName="t122",targetState="execPlannedMoves",cond=whenTimeout("local_tout_trolley_doMove"))   
					transition(edgeName="t123",targetState="working",cond=whenDispatchGuarded("trolleycmd",{home == 1 && terminate == 0 
					}))
				}	 
				state("finishPlannedMoves") { //this:State
					action { //it:State
						 var pos = itunibo.planner.plannerUtil.get_curPos().toString()  
						println("trolley [finishPlannedMoves] | pos = $pos")
						if( pos.equals("(6, 0)") 
						 ){forward("weight", "weight(0)" ,"weightsensor" ) 
						}
						if( pos.equals("(6, 4)") 
						 ){forward("outsonarocc", "outsonarocc(0)" ,"outsonar" ) 
						forward("outsonarocc", "outsonarocc(0)" ,"parkingservicegui" ) 
						}
						println("trolley [finishPlannedMoves] | finish")
					}
					 transition( edgeName="goto",targetState="end", cond=doswitchGuarded({ terminate == 1   
					}) )
					transition( edgeName="goto",targetState="idle", cond=doswitchGuarded({! ( terminate == 1   
					) }) )
				}	 
				state("end") { //this:State
					action { //it:State
						println("trolley | end")
							var direction= itunibo.planner.plannerUtil.getDirection()
									
									if(direction == "leftDir"){
										forward("cmd", "cmd(l)" ,"basicrobot" )
										itunibo.planner.plannerUtil.updateMap( "l"  )
									}else{
										forward("cmd", "cmd(l)" ,"basicrobot" )
										itunibo.planner.plannerUtil.updateMap( "l"  ) 
										forward("cmd", "cmd(l)" ,"basicrobot" )
										itunibo.planner.plannerUtil.updateMap( "l"  ) 
									} 
					}
				}	 
			}
		}
}
