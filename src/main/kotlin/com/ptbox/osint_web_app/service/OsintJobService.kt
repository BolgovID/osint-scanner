package com.ptbox.osint_web_app.service

interface OsintJobService {
    fun initScanning(domain: String): String
    fun processScanLogs(containerId: String)
    fun stopScanning(containerId: String)
}