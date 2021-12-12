package  it.unibo.parkingmanagergui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@EnableWebMvc		//DO NOT USE: disbale satic
//@EnableWebMvc will disable org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration
class ParkingManagerGui

fun main(args: Array<String>) {
	runApplication<ParkingManagerGui>(*args)
}
