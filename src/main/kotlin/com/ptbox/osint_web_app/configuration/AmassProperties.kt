package com.ptbox.osint_web_app.configuration

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "docker.amass")
data class AmassProperties(
    val name: String,
    val tag: String
) {
    fun getImageIdentifier(): String {
        return "$name:$tag"
    }
}