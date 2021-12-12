package  it.unibo.parkingmanagergui.connQak

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import it.unibo.kactor.ApplMessage

class connQakMqtt( ) : connQakBase( ), MqttCallback{
 	lateinit var client  : MqttClient
 	val clientid         = "clientmqtt"
	val answerTopic      = mqtttopic

	override fun messageArrived(topic: String, msg: MqttMessage) {

        val m = ApplMessage( msg.toString() )
        println("       %%% connQakMqtt |  ARRIVED on $topic  m=$m  " )
    }
	override fun connectionLost(cause: Throwable?) {
        println("       %%% connQakMqtt |  MQTT connectionLost $cause " )
    }
    override fun deliveryComplete(token: IMqttDeliveryToken?) {

    }
	
	fun publish( msg: String, topic: String, qos: Int = 1, retain: Boolean = false) {
		val message = MqttMessage()
		message.setRetained(retain)
		if (qos == 0 || qos == 1 || qos == 2) {

			message.setQos(0)
		}
		message.setPayload(msg.toByteArray())
		try {
			println("mqtt publish $msg on $topic")
			client.publish(topic, message)
		} catch (e:Exception) {
			println("       %%% connQakMqtt | publish ERROR $e topic=$topic msg=$msg"  )
 		}
	}
	
	override fun createConnection(   ){
		val brokerAddr = "tcp://$mqtthostAddr:$mqttport"
		try {

			client = MqttClient(brokerAddr , clientid )
			val options = MqttConnectOptions()
			options.setKeepAliveInterval(480)
			options.setWill("unibo/clienterrors", "crashed".toByteArray(), 2, true)
			client.connect(options)
			println("       %%% connQakMqtt | connect DONE $clientid to $brokerAddr " )//+ " " + client

			client.setCallback(this)
			client.subscribe(answerTopic)
			
		} catch (e: Exception) {
			println("       %%% connQakMqtt | for $clientid connect ERROR for: $brokerAddr" )
 		}
	}
	
	override fun forward(  msg: ApplMessage){
 		publish(msg.toString(), mqtttopic)		
	}
	
	override fun request(  msg: ApplMessage){
 		publish(msg.toString(), mqtttopic)

	}
	
	override fun emit( msg: ApplMessage ){
 		publish(msg.toString(), mqtttopic )		
	}

	override fun requestWithRensponse(msg: ApplMessage): String {
		TODO("Not yet implemented")
	}

}