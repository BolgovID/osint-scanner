package com.ptbox.osint_web_app.entity

enum class ScanStatus() {
    PROCESSING,
    SUCCESS,
    FAILED,
    TERMINATED;

    fun isTerminal(): Boolean {
        return this in listOf(SUCCESS, FAILED, TERMINATED)
    }
}