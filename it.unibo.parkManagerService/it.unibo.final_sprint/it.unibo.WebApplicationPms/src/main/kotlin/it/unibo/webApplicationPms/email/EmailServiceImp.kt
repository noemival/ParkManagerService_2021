package it.unibo.webApplicationPms.email

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

@Service
class EmailServiceImp(
    private val javaMailSender: JavaMailSender){

    fun sendEmail(email: String, tokenid: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.setSubject("Cark Confirm")

       val link= "http://localhost:8081/pickup/"+tokenid
       println(link)
        message.setText("Grazie di aver scelto Cark, Ecco il tuo link per prelevare la macchina "+link)
        javaMailSender.send(message)
    }
}