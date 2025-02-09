package com.ptbox.osint_web_app.dto

import jakarta.validation.constraints.Pattern

data class ScanRequest(
    @field:Pattern(
        regexp = "^(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",
        message = "Not a domain."
    )
    val domain: String
)
