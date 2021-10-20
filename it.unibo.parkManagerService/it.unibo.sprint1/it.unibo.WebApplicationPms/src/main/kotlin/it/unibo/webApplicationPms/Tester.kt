package it.unibo.webApplicationPms
import org.apache.commons.io.IOUtils
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import java.lang.Exception
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.BasicNameValuePair
import org.apache.http.NameValuePair
import java.util.ArrayList

object Tester {
    var i = 0
    val strUrl = "http://localhost:8081/movetrolley"
    val client: HttpClient = HttpClientBuilder.create().build()
    val request = HttpPost(strUrl)

    fun doPostWithParams(){
        try {

            sendMessage("req(moveToIn)")
            sendMessage("req(moveToHome)")
            Thread.sleep(5800)
            sendMessage("req(moveToSlotIn)")
            sendMessage("req(moveToHome)")
            Thread.sleep(3500)
            sendMessage("req(moveToSlotOut)")
            sendMessage("req(moveToOut)")
            sendMessage("req(end)")


        } catch (ex: Exception) {
            println(ex.message)
        }
    }
    fun sendMessage(message : String){


        val params: MutableList<NameValuePair> = ArrayList()
        params.add(BasicNameValuePair("move", message))

        request.setEntity(UrlEncodedFormEntity(params))
        var response: HttpResponse = client.execute(request)
        var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
        println("RESPONSE"+ i++ +"=$answer")
        params.clear()
    }
    @JvmStatic
    fun main(args: Array<String>) {
        //doSimplePost()
        doPostWithParams()
    }
}