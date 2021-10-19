

object TrolleyPlannerSupport{
	

	fun setGoal( goal : String ){
		
		when(goal){
			"moveToIn" -> itunibo.planner.plannerUtil.planForGoal("6","0")
			"moveToSlotIn" ->  itunibo.planner.plannerUtil.planForGoal("4","1")
			"moveToSlotOut" -> itunibo.planner.plannerUtil.planForGoal("4","2")
			"moveToOut"  -> itunibo.planner.plannerUtil.planForGoal("6","4")
			"moveToHome" -> itunibo.planner.plannerUtil.planForGoal("0","0")
			"end" -> itunibo.planner.plannerUtil.planForGoal("0","0")
		}
	}
	fun initPlanner(mapname: String){
			itunibo.planner.plannerUtil.initAI(  )
			println("&&&  trolley loads the parking map from the given file ...")
			itunibo.planner.plannerUtil.loadRoomMap( "$mapname"  )
			itunibo.planner.plannerUtil.showMap(  )
			itunibo.planner.plannerUtil.showCurrentRobotState(  )
	}
	
	fun getNextMove(): String{
		var move = itunibo.planner.plannerUtil.getNextPlannedMove()
		if(move.length>0){		
			itunibo.planner.plannerUtil.updateMap(  "$move" )
			itunibo.planner.plannerUtil.showCurrentRobotState( )
		}
		
		return move
	}
	fun atHome(): String{
		var direction= itunibo.planner.plannerUtil.getDirection()
		if(direction == "leftDir"){
			itunibo.planner.plannerUtil.updateMap( "l"  )
		}else{
			itunibo.planner.plannerUtil.updateMap( "l"  ) 
			itunibo.planner.plannerUtil.updateMap( "l"  ) 
		}
		itunibo.planner.plannerUtil.showCurrentRobotState( )
			return direction
	}

}