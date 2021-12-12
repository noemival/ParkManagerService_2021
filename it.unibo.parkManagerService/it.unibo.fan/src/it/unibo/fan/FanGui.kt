package it.unibo.fan
import java.net.Socket
import java.util.*
import javax.swing.JFrame
import javax.swing.JLabel

class FanGui {

   private val frame1: JFrame? = JFrame()

   private val labelValue : JLabel =  JLabel("State")
   
	public fun initialiaze() {
       frame1!!.setBounds(100, 100, 467, 392)
       frame1!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
       frame1!!.contentPane.layout = null
       frame1!!.isVisible = true


        labelValue.setBounds(150,100, 117, 40)
        labelValue.setEnabled(true)
        frame1.getContentPane().add(labelValue)

   }
	public fun setState(text:String){
		labelValue.setText(text)
	}
	
        

}