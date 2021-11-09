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
import org.apache.http.client.methods.HttpGet
import java.util.ArrayList

object Tester {
    var i = 0
    val strUrl = "http://localhost:8081//parkmanagerservice"
    val client: HttpClient = HttpClientBuilder.create().build()
    //val request = HttpGet(strUrl)
    var slotNum =0
    var tokenid = 0L
    fun doPostWithParams(){
        try {
            notifyIn()
            Thread.sleep(800)
            carenter()
            Thread.sleep(800)
          //  Thread.sleep(5800)
            pickup()
          //  sendMessage("req(moveToHome)")
          //  Thread.sleep(5800)
          //  Thread.sleep(5800)
          //  sendMessage("notifyIn(V)")
          //  Thread.sleep(5800)
          //  sendMessage("req(pickup)")
           // sendMessage("req(moveToSlotOut)")

        //     sendMessage("req(moveToHome)")
          //  Thread.sleep(3500)
           // sendMessage("req(moveToSlotOut)")
            //sendMessage("req(moveToOut)")
           // sendMessage("req(end)")


        } catch (ex: Exception) {
            println(ex.message)
        }
    }
    fun notifyIn(){


       // val params: MutableList<NameValuePair> = ArrayList()
        //params.add(BasicNameValuePair("move", message))

       // request.setEntity(UrlEncodedFormEntity(params))
        var response: HttpResponse = client.execute(HttpGet(strUrl+"/slotnum"))
        var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
        slotNum= answer.toInt()
        println("RESPONSE"+"=$answer")
       // params.clear()
    }
    fun carenter(){


        // val params: MutableList<NameValuePair> = ArrayList()
        //params.add(BasicNameValuePair("move", message))

        // request.setEntity(UrlEncodedFormEntity(params))
        if(slotNum!=0) {
            var response: HttpResponse = client.execute(HttpGet(strUrl + "/slotnum=" + slotNum))
            var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("RESPONSE"  + "=$answer")
            tokenid = answer.toLong()
        }
        // params.clear()
    }
    fun pickup(){


        // val params: MutableList<NameValuePair> = ArrayList()
        //params.add(BasicNameValuePair("move", message))

        // request.setEntity(UrlEncodedFormEntity(params)) if(tokenid!=0) {

            var response: HttpResponse = client.execute(HttpGet(strUrl + "/tokenid="+ tokenid))
            var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
            println("RESPONSE" + "=$answer")


        // params.clear()
    }
    fun sendMessage(message : String){


        // val params: MutableList<NameValuePair> = ArrayList()
        //params.add(BasicNameValuePair("move", message))

        // request.setEntity(UrlEncodedFormEntity(params))
        var response: HttpResponse = client.execute(HttpPost(strUrl))
        var answer: String = IOUtils.toString(response.getEntity().getContent(), "UTf-8")
        println("RESPONSE"+ i++ +"=$answer")
        // params.clear()
    }
    @JvmStatic
    fun main(args: Array<String>) {
        //doSimplePost()
        doPostWithParams()
    }
}