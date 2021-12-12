package it.unibo.webApplicationPms.utility
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.channels.Channel

class parkmanagerserviceProxy (name : String ) : ActorBasic( name ) {
    var answerMoveChannel = Channel<String>()

    init{

    }
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
        elabData( msg )
    }


    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend fun elabData( msg: ApplMessage ){
        sysUtil.colorPrint("parkmanagerserviceProxy y | elabData:${msg} ", Color.BLUE)

    }

}