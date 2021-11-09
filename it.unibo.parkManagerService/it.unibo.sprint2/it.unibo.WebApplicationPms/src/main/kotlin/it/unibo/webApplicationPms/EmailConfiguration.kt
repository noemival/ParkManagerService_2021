package it.unibo.webApplicationPms

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "email")
class EmailConfiguration {
    var to: String? = null
    var subject: String? = null
    var body: String? = null
}