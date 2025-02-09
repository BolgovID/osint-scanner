package com.ptbox.osint_web_app.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(
    @Value("\${frontend.url}") private val frontEndUrl: String
) : WebMvcConfigurer {


    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedOrigins(frontEndUrl)
            .allowedMethods("*")
    }
}