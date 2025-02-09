package com.ptbox.osint_web_app

import com.ptbox.osint_web_app.configuration.AmassProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AmassProperties::class)
class OsintWebAppApplication

fun main(args: Array<String>) {
    runApplication<OsintWebAppApplication>(*args)
}
