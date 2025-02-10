package com.ptbox.osint_web_app.service.impl

import com.ptbox.osint_web_app.entity.ScanStatus
import com.ptbox.osint_web_app.service.OsintJobService
import com.ptbox.osint_web_app.service.ScanInfoService
import org.slf4j.LoggerFactory

abstract class OsintToolLifecycleService(
    private val scanInfoService: ScanInfoService
) : OsintJobService {
    private val logger = LoggerFactory.getLogger(OsintToolLifecycleService::class.java)

    fun onSuccess(containerId: String) {
        scanInfoService.updateStatusByContainerId(containerId, ScanStatus.SUCCESS)
        logger.info("Scan completed successfully for container $containerId")
    }

    fun onFailure(containerId: String, reason: String) {
        scanInfoService.updateStatusByContainerId(containerId, ScanStatus.FAILED)
        logger.error("Scan failed for container $containerId: $reason")
    }

    fun onTerminated(containerId: String) {
        scanInfoService.updateStatusByContainerId(containerId, ScanStatus.TERMINATED)
        logger.info("Scan cancelled for container $containerId")
    }
}