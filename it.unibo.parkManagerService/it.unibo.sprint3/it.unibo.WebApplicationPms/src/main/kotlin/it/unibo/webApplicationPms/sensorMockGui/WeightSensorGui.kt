package it.unibo.webApplicationPms.sensorMockGui


import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.*


object WeightSensorGui {

    private val frame1: JFrame? = JFrame()
    private val btnDetect: JButton? = JButton("Car Detected")
    private val labelValue : JLabel =  JLabel("insert value: ")
    private val textField :  JTextField = JTextField("")
    private var connected = false

    fun initialiaze() {
        frame1!!.setBounds(100, 100, 467, 392)
        frame1!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame1!!.contentPane.layout = null
        frame1!!.isVisible = true
        var cancelReconnect: AtomicBoolean = AtomicBoolean(false)


        val client: Mqtt3AsyncClient = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .serverPort(1883)
                .buildAsync()

        btnDetect!!.addActionListener {
            if(textField.getText().trim().length!=0) {
                if (!textField.getText().contains("-")) {
                    try {
                        if(connected == false) {
                            client.connect()
                                    .whenComplete { connAck, throwable ->
                                        if (throwable != null) {
                                            println("Connection refused"+ throwable)
                                            connected = true
                                        } else {
                                            publish(client, textField.getText())
                                        }
                                    }
                        }else{
                            publish(client, textField.getText())
                        }
                    } catch (e: NumberFormatException) {
                        JOptionPane.showMessageDialog(null, "Insert Number", "InfoBox", JOptionPane.ERROR_MESSAGE)
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Insert Number", "InfoBox", JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(frame1, "Insert value before press the button", "InfoBox:", JOptionPane.ERROR_MESSAGE)
            }

        }
        btnDetect.setBounds(140, 150, 117, 29)
        btnDetect.setEnabled(true)
        frame1.getContentPane().add(btnDetect)

        labelValue.setBounds(100,100, 117, 40)
        labelValue.setEnabled(true)
        frame1.getContentPane().add(labelValue)

        textField.setBounds(180,100, 117, 40)
        textField.setEnabled(true)
        frame1.getContentPane().add(textField)
    }

    private fun publish(client: Mqtt3AsyncClient, value : String){
        client.publishWith()
                .topic("weightsensor/data")
                .payload(value.toByteArray())
                .qos(MqttQos.EXACTLY_ONCE)
                .send()
    }
    @JvmStatic
    fun main(args: Array<String>) {
        initialiaze()
    }
}