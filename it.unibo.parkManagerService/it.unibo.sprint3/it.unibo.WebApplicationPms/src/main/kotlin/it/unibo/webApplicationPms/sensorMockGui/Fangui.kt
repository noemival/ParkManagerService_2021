package it.unibo.webApplicationPms.sensorMockGui

import java.net.Socket
import java.util.*
import javax.swing.JFrame
import javax.swing.JLabel

object FanGui {

        private val frame1: JFrame? = JFrame()

        private val labelValue : JLabel =  JLabel("State")
        fun initialiaze() {
            frame1!!.setBounds(100, 100, 467, 392)
            frame1!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame1!!.contentPane.layout = null
            frame1!!.isVisible = true


            labelValue.setBounds(150,100, 117, 40)
            labelValue.setEnabled(true)
            frame1.getContentPane().add(labelValue)


            val clientSocket = Socket("127.0.0.1", 6090)

          while (true) {
              val inFromServer = Scanner(clientSocket.getInputStream())
              //  val inFromClient = Scanner(connectionSocket.getInputStream())
              val input = inFromServer.nextLine();
              println(input)
              labelValue.text = input
          }
          clientSocket.close()
        }


        @JvmStatic
        fun main(args: Array<String>) {
            initialiaze()
        }

}