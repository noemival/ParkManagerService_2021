package it.unibo.webApplicationPms

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CarkApplication

    fun main(args: Array<String>) {
        runApplication<CarkApplication>(*args)


    }
