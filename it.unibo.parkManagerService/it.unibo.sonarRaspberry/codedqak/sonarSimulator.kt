//package rx
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking

/*
-------------------------------------------------------------------------------------------------
 
-------------------------------------------------------------------------------------------------
 */

class sonarSimulator ( name : String ) : ActorBasic( name ) {
  
	val data = sequence<Int>{
		var v0 = 80
		yield(v0)
		while(true){
			v0 = v0 - 5
			yield( v0 )
		}
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : ApplMessage){
  		println("$tt $name | received  $msg "  )
		if( msg.msgId() == "simulatorstart") startDataReadSimulation(   )
     }
  	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun startDataReadSimulation(    ){
  			var i = 0
		/*	while( i < 2 ){
 	 			val m1 = "distance( ${data.elementAt(i*2)} )"
				i++
 				val event = MsgUtil.buildEvent( name,"sonar",m1)								
  				emitLocalStreamEvent( event )
 				//println("$tt $name | generates $event")
 				//emit(event)  //APPROPRIATE ONLY IF NOT INCLUDED IN A PIPE
 				delay( 500 )
  			}*/
 			val m1 = "distance( ${data.elementAt(15)} )"
  			val event = MsgUtil.buildEvent( name,"sonar",m1)								
  				emitLocalStreamEvent( event )
  				delay( 2000 )
  			val m2 = "distance( ${data.elementAt(50)} )"
  			val event2 = MsgUtil.buildEvent( name,"sonar",m2)								
  				emitLocalStreamEvent( event2 )
			terminate()
	}

} 

//@kotlinx.coroutines.ObsoleteCoroutinesApi
//@kotlinx.coroutines.ExperimentalCoroutinesApi
//fun main() = runBlocking{
// //	val startMsg = MsgUtil.buildDispatch("main","start","start","datasimulator")
//	val consumer  = dataConsumer("dataconsumer")
//	val simulator = sonarSimulator( "datasimulator" )
//	val filter    = dataFilter("datafilter", consumer)
//	val logger    = dataLogger("logger")
//	simulator.subscribe( logger ).subscribe( filter ).subscribe( consumer ) 
//	MsgUtil.sendMsg("start","start",simulator)
//	simulator.waitTermination()
// } 