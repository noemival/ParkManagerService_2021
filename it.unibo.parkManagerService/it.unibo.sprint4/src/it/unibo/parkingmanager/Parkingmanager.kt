/* Generated by AN DISI Unibo */ 
package it.unibo.parkingmanager

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkingmanager ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var fanIsStarted = 0
			  var robotIsActive = 1 
			  var stateTrolley = "trolley(idle)"
			  var stateFan= "fan(stop)"
			  var start =  0L
			  var differenza = 0L
			  var temp1 = 0 
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("parkingmanager | start")
					}
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("waiting") { //this:State
					action { //it:State
						delay(500) 
						println("parkingmanager | waiting")
						 stateTrolley = utility.HandleData().getState("trolley") 
						println("$stateTrolley")
						updateResourceRep("{\"statetrolley\":\"$stateTrolley\", \"statefan\":\"$stateFan\", \"temp\":\"temp($temp1)\"}" 
						)
					}
					 transition(edgeName="t011",targetState="handleTemperature",cond=whenDispatch("temperature"))
				}	 
				state("handleTemperature") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("temperature(T)"), Term.createTerm("temperature(T)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								delay(500) 
								  temp1= payloadArg(0).toInt()
												stateTrolley = utility.HandleData().getState("trolley")
												stateFan = utility.HandleData().getState("fanmanager")
								delay(400) 
								updateResourceRep("{\"statetrolley\":\"$stateTrolley\", \"statefan\":\"$stateFan\", \"temp\":\"temp($temp1)\"}" 
								)
								println("parkingmanager [handleTemperature] | state trolley = $stateTrolley state fan = $stateFan ")
								println("parkingmanager [handleTemperature] | temperature = $temp1 ")
						}
						stateTimer = TimerActor("timer_handleTemperature", 
							scope, context!!, "local_tout_parkingmanager_handleTemperature", 4000.toLong() )
					}
					 transition(edgeName="t012",targetState="autoFan",cond=whenTimeout("local_tout_parkingmanager_handleTemperature"))   
					transition(edgeName="t013",targetState="handleTemperature",cond=whenDispatch("temperature"))
					transition(edgeName="t014",targetState="handleTrolley",cond=whenDispatch("stateChange"))
				}	 
				state("handleTrolley") { //this:State
					action { //it:State
						println("parkingmanager | handleTemperature troll = $temp1 ")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("stateChange(V)"), Term.createTerm("stateChange(V)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								var stateT= payloadArg(0) 
								if(  temp1 > 35 && !stateTrolley.equals("trolley(stopped)") && stateT.equals("over")  && stateFan.equals("fan(stop)") 
								 ){println("parkingmanager | trolley stopped update fan")
								forward("trolleystop", "trolleystop(V)" ,"trolley" ) 
								forward("fanstart", "fanstart(F)" ,"fanmanager" ) 
								}
								if(  temp1 < 35  && stateTrolley.equals("trolley(stopped)") && stateT.equals("under")  && stateFan.equals("fan(work)") 
								 ){println("parkingmanager | trolley working update fan ")
								forward("trolleyresume", "trolleyresume(V)" ,"trolley" ) 
								forward("fanstop", "fanstop(F)" ,"fanmanager" ) 
								}
								 stateTrolley = utility.HandleData().getState("trolley")
												stateFan = utility.HandleData().getState("fanmanager")
								delay(400) 
								updateResourceRep("{\"statetrolley\":\"$stateTrolley\", \"statefan\":\"$stateFan\", \"temp\":\"temp($temp1)\"}" 
								)
						}
						stateTimer = TimerActor("timer_handleTrolley", 
							scope, context!!, "local_tout_parkingmanager_handleTrolley", 4000.toLong() )
					}
					 transition(edgeName="t015",targetState="autoFan",cond=whenTimeout("local_tout_parkingmanager_handleTrolley"))   
					transition(edgeName="t016",targetState="handleTemperature",cond=whenDispatch("temperature"))
				}	 
				state("autoFan") { //this:State
					action { //it:State
						if(  temp1 > 35  && stateFan.equals("fan(stop)") 
						 ){println("parkingmanager [autoFan] | fan working ")
						forward("fanstart", "fanstart(F)" ,"fanmanager" ) 
						}
						if(  temp1 < 35  && stateFan.equals("fan(work)")   
						 ){println("parkingmanager [autoFan] | fan stopped ")
						forward("fanstop", "fanstop(F)" ,"fanmanager" ) 
						}
						 stateTrolley = utility.HandleData().getState("trolley")
										stateFan = utility.HandleData().getState("fanmanager")
						delay(400) 
						updateResourceRep("{\"statetrolley\":\"$stateTrolley\", \"statefan\":\"$stateFan\", \"temp\":\"temp($temp1)\"}" 
						)
					}
					 transition(edgeName="t017",targetState="handleTemperature",cond=whenDispatch("temperature"))
					transition(edgeName="t018",targetState="handleTrolley",cond=whenDispatch("stateChange"))
				}	 
			}
		}
}
