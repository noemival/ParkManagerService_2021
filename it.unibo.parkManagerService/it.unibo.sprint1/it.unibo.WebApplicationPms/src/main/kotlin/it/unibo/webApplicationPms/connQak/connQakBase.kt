package it.unibo.webApplicationPms.connQak
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import it.unibo.kactor.ApplMessage
 
enum class ConnectionType {
    TCP, HTTP
}

abstract class connQakBase() {
lateinit var currQakConn  : connQakBase
	
	companion object{
	fun create(connType: ConnectionType) : connQakBase {
		  showSystemInfo()
		  when( connType ){
				 ConnectionType.TCP ->   {return connQakTcp( )
                 }
  				 ConnectionType.HTTP ->  {return connQakHttp( )
                 }
//   				 else -> //println("WARNING: protocol unknown")
 		  }		
	}
	fun showSystemInfo(){
		println(
			"connQakBase  | COMPUTER memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		println(
			"connQakBase  | NUM of threads="+ Thread.activeCount() +
					" currentThread=" + Thread.currentThread() );
	}
	}//object

	
	  abstract fun createConnection(   )
      abstract fun forward( msg : ApplMessage )
      abstract fun request( msg : ApplMessage )
      abstract fun emit( msg : ApplMessage )
      abstract fun requestWithRensponse( msg: ApplMessage ) : String


	
}

 
 
 