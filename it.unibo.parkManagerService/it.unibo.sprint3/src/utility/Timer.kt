object Timer{
	 var terminated= false
	var strattime= 0L
	var dd=0L

fun timerstart(){
	  strattime   = System.currentTimeMillis()
        Thread.sleep(1000)
        dd = System.currentTimeMillis() - strattime
        //Runtime.getRuntime().exec("sudo ./sleepcrono")
        //println("TimerThread  RESUMES after:${dd}
	
	
	 /*starttime     = System.currentTimeMillis()
        Thread.sleep(tout)
        val dd = System.currentTimeMillis() - starttime
        //Runtime.getRuntime().exec("sudo ./sleepcrono")
        //println("TimerThread  RESUMES after:${dd} ")
        if( ! terminated ){
            val msgEv = MsgUtil.buildEvent("timer",ev,ev)
            runBlocking {
                myactor.actor.send(msgEv)
            }
            //MsgUtil.sendMsg("timer",ev,ev,"basicrobot", null)
            println("TimerActor $name has EMITTED :${ev} ")
        }else{
            //println("TimerActor $name ENDS without emitting since terminated after: $Duration")
        }*/
}
	fun EndTime():Boolean {
		if(dd==1000L){
			println("$dd")
			return true
		}else {
			println("$dd")
	return false		}		

	}
}