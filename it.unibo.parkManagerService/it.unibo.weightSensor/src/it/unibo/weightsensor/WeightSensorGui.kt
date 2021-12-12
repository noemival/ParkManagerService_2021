package it.unibo.weightsensor


//import com.hivemq.client.mqtt.MqttClient
//import com.hivemq.client.mqtt.datatypes.MqttQos
//import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.*


class WeightSensorGui {

    private val frame1: JFrame? = JFrame()
    private val btnDetect: JButton? = JButton("Car Detected")
    private val labelValue : JLabel =  JLabel("insert value: ")
    private val textField :  JTextField = JTextField("")
    private var connected = false
	var value = 0

    public fun initialiaze() {
        frame1!!.setBounds(100, 100, 467, 392)
        frame1!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame1!!.contentPane.layout = null
        frame1!!.isVisible = true
        


       
        btnDetect!!.addActionListener {
            if(textField.getText().trim().length!=0) {
                if (!textField.getText().contains("-")) {
                    try {
						
                       this.value = textField.getText().toInt()
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

   
//    @JvmStatic
//    fun main(args: Array<String>) {
//      initialiaze()
//    }
}