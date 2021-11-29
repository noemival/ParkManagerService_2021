import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
	java
	application
	jacoco
	distribution

}

group = "it.unibo"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_11
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions {
		jvmTarget = "11"
	}
}
repositories {
	mavenCentral()
	jcenter()
	flatDir{ dirs("../unibolibs")   }   //Our libraries
}

dependencies {


	implementation("com.hivemq:hivemq-mqtt-client:1.2.2")
	implementation ("de.smartsquare:mqtt-starter:0.14.0")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	// implementation("org.springframework.boot:spring-boot-starter-security")
	//implementation("org.springframework.boot:spring-boot-starter-security")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation( "org.springframework.boot:spring-boot-starter-tomcat")
	implementation("org.springframework.boot:spring-boot-starter-mail:1.2.0.RELEASE")

	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

	implementation("com.google.guava:guava:29.0-jre")

//	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.0.4.RELEASE")

	implementation("org.thymeleaf:thymeleaf-spring5:3.0.12.RELEASE")
	//compile("org.springframework.boot:spring-boot-starter-thymeleaf")
	//COROUTINE
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.1.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")
	//coap
	implementation("org.eclipse.californium:californium-core:2.0.0-M12")
	//JSON
	// https://mvnrepository.com/artifact/org.json/json
	implementation("org.json:json:20201115" )
	//OkHttp library for websockets with Kotlin
	implementation("com.squareup.okhttp3:okhttp:4.9.0")

//OkHttp library for websockets with Kotlin
	implementation( "com.squareup.okhttp3:okhttp:4.9.0" )

//ADDED FOR THE HTTP CLIENT
	// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
	implementation ("org.apache.httpcomponents:httpclient:4.5")
	// https://mvnrepository.com/artifact/commons-io/commons-io
	implementation ("commons-io:commons-io:2.6")

//STRING COLORS
	// https://mvnrepository.com/artifact/com.andreapivetta.kolor/kolor
	implementation( "com.andreapivetta.kolor:kolor:1.0.0" )

//PLANNER aimacode
	implementation("com.googlecode.aima-java:aima-core:3.0.0")

	//UNIBO
	implementation("IssActorKotlinRobotSupport:IssActorKotlinRobotSupport:2.0")
	implementation("uniboIssSupport:IssWsHttpJavaSupport:1.0")
	implementation("uniboInterfaces:uniboInterfaces")
	implementation("uniboProtocolSupport:unibonoawtsupports")
	implementation("2p301:2p301")
	implementation("it.unibo.qakactor-2.4:it.unibo.qakactor-2.4")
//MQTT
// https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3
	implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.1")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
/*application {
	// Define the main class for the application.
	mainClass.set("it.unibo.webApplicationPms.CarKApplication")
}*/
tasks.withType<Test> {
	useJUnitPlatform()
}
version = "1.0.1"

/*tasks.jar {
	manifest {
		attributes["Main-Class"] = "it.unibo.webApplicationPms.CarkApplication"
		attributes(mapOf("Implementation-Title" to project.name,
			"Implementation-Version" to project.version))
	}*/
//}
