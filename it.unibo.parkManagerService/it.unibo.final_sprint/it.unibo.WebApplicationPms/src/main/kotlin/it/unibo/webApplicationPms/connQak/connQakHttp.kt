package it.unibo.webApplicationPms.connQak

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil

import org.apache.http.impl.client.HttpClientBuilder

import java.io.InputStream
import org.apache.http.impl.client.CloseableHttpClient
import it.unibo.kactor.ApplMessage


class connQakHttp( ) : connQakBase( ){
lateinit var client   : CloseableHttpClient  
lateinit var hostAddr : String 
	 
 		
	override fun createConnection(   ){
		hostAddr =  "http://$apphostaddress:$apphostport"
		println("connQakHttp | createConnection hostAddr=$")
		client   =  HttpClientBuilder.create().build()
		sysUtil.colorPrint("connQakHttp | connected with $apphostaddress", Color.GREEN)
	}
	override fun forward(  msg: ApplMessage){
 		 	
	}
	
	override fun request(  msg: ApplMessage){
 	}
	
	override fun emit( msg: ApplMessage ){
  	}

	override fun requestWithRensponse(msg: ApplMessage): String {
		TODO("Not yet implemented")
	}


//	override fun forward( move : String ){
//		println("connQakHttp | forward $move")
//		val response = Tester.execute( HttpPost(hostAddr+"/$move")) //HttpResponse
//		showResponse( "POST $move", response.getEntity().getContent() )
//		//println("connQakHttp | response $response")
//	}
//	
//	override fun request( move : String ){
//		println("connQakHttp | request $move")
//		val post = HttpPost(hostAddr+"/$move")		     //HttpPost
//		post.setHeader("Content-Type","text/plain")		//could be "application/json"
//		post.setHeader("Accept", "text/plain")
//		val response = Tester.execute( post )           //HttpResponse
//		//println("connQakHttp | response ${response.getStatusLine().getStatusCode()}")
//		showResponse( "POST $move", response.getEntity().getContent() )
// 	}
//	
//	override fun emit( ev : String ){
//		println("connQakHttp | emit $ev")
//		val response = Tester.execute( HttpPost(hostAddr+"/$ev")) //HttpResponse
//		showResponse( "POST $ev", response.getEntity().getContent() )
// 	}
	
	fun showResponse(  msg : String, inps : InputStream ){
		inps.bufferedReader().use {
			val response = StringBuffer()
			var inputLine = it.readLine()
			while (inputLine != null) {
				response.append(inputLine)
				inputLine = it.readLine()
			}
			it.close()
			println(msg)
			println(response.toString())
		}	
	}
	
	
	
}