package it.unibo.webApplicationPms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebApplicationPms

fun main(args: Array<String>) {
	runApplication<WebApplicationPms>(*args)
}
