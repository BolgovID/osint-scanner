package com.ptbox.osint_web_app.service

import com.ptbox.osint_web_app.dto.ScanDto
import com.ptbox.osint_web_app.dto.ScanWithDetailsDto
import com.ptbox.osint_web_app.exception.DomainAlreadyScanningException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Service
class DomainScanFacade(
    private val osintJobService: OsintJobService,
    private val scanInfoService: ScanInfoService
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val domainLocks = ConcurrentHashMap<String, ReentrantLock>()


    fun scanDomain(domain: String): ScanDto {
        val lock = domainLocks.computeIfAbsent(domain) { ReentrantLock() }
        lock.withLock {
            val targetDomainInProcessingState = scanInfoService.findScansInProcessingState()
                .any { it.domain == domain }

            if (targetDomainInProcessingState) {
                throw DomainAlreadyScanningException("The domain $domain is already being scanned.")
            }
            val containerId = osintJobService.initScanning(domain)
            val scanInfo = scanInfoService.createScanInfo(containerId, domain)
            scope.launch {
                osintJobService.processScanLogs(containerId)
            }
            return ScanDto.create(scanInfo)
        }
    }

    fun stopScanning(scanId: Long): ScanDto {
        val scanInfo = scanInfoService.findByScanId(scanId)
        osintJobService.stopScanning(scanInfo.containerId)
        return ScanDto.create(scanInfo)
    }

    fun findAllScans(): List<ScanDto> {
        return scanInfoService.findAll().map(ScanDto.Companion::create)
    }

    fun getScanDetails(scanId: Long): ScanWithDetailsDto {
        val scanInfo = scanInfoService.findByScanId(scanId)
        return ScanWithDetailsDto.create(scanInfo)
    }
}