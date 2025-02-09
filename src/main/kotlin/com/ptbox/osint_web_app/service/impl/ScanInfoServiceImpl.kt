package com.ptbox.osint_web_app.service.impl

import com.ptbox.osint_web_app.entity.ScanInfo
import com.ptbox.osint_web_app.entity.ScanStatus
import com.ptbox.osint_web_app.exception.EntityNotFoundException
import com.ptbox.osint_web_app.repository.ScanInfoRepository
import com.ptbox.osint_web_app.service.ScanInfoService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ScanInfoServiceImpl(
    private val scanInfoRepository: ScanInfoRepository,
) : ScanInfoService {

    @Transactional
    override fun saveLog(containerId: String, log: String) {
        val scanInfo = scanInfoRepository.findByContainerId(containerId)
            .orElseThrow { EntityNotFoundException("ScanInfo with id $containerId not found") }

        scanInfo.addLog(log)
        scanInfoRepository.save(scanInfo)
    }

    @Transactional
    override fun createScanInfo(containerId: String, domain: String): ScanInfo {
        val scanInfo = ScanInfo(
            containerId = containerId,
            domain = domain,
        )
        return scanInfoRepository.save(scanInfo)
    }

    @Transactional
    override fun updateStatusByContainerId(containerId: String, status: ScanStatus): ScanInfo {
        val scanInfo = scanInfoRepository.findByContainerId(containerId)
            .orElseThrow { EntityNotFoundException("ScanInfo with container id $containerId not found") }

        if (scanInfo.status.isTerminal()) {
            return scanInfo
        }

        scanInfo.status = status
        scanInfo.endTime = Instant.now()
        return scanInfoRepository.save(scanInfo)
    }

    @Transactional(readOnly = true)
    override fun findByScanId(scanId: Long): ScanInfo {
        return scanInfoRepository.findById(scanId)
            .orElseThrow { EntityNotFoundException("ScanInfo with id $scanId not found") }
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<ScanInfo> {
        return scanInfoRepository.findAllByOrderByStartTimeDesc()
    }

    @Transactional
    override fun findScansInProcessingState(): List<ScanInfo> {
        return scanInfoRepository.findByStatus(ScanStatus.PROCESSING)
    }
}