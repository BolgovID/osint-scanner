package com.ptbox.osint_web_app.service

import com.ptbox.osint_web_app.entity.ScanInfo
import com.ptbox.osint_web_app.entity.ScanStatus

interface ScanInfoService {
    fun saveLog(containerId: String, log: String)
    fun createScanInfo(containerId: String, domain: String): ScanInfo
    fun updateStatusByContainerId(containerId: String, status: ScanStatus): ScanInfo
    fun findByScanId(scanId: Long): ScanInfo
    fun findAll(): List<ScanInfo>
    fun findScansInProcessingState(): List<ScanInfo>
}