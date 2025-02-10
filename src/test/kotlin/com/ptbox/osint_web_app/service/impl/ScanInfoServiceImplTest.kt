package com.ptbox.osint_web_app.service.impl

import com.ptbox.osint_web_app.entity.ScanInfo
import com.ptbox.osint_web_app.entity.ScanStatus
import com.ptbox.osint_web_app.exception.EntityNotFoundException
import com.ptbox.osint_web_app.repository.ScanInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ScanInfoServiceImplTest {

    private var scanInfoRepository: ScanInfoRepository = mockk(relaxed = true)
    private lateinit var scanInfoService: ScanInfoServiceImpl

    @BeforeEach
    fun setUp() {
        scanInfoService = ScanInfoServiceImpl(scanInfoRepository)
    }

    @Test
    fun testSaveLogShouldAddLogToScanInfo() {
        val containerId = "dummyContainerId"
        val log = "dummy log"
        val domain = "dummy.com"
        val scanInfo = ScanInfo(containerId = containerId, domain = domain)

        every { scanInfoRepository.findByContainerId(containerId) } returns Optional.of(scanInfo)
        every { scanInfoRepository.save(scanInfo) } returns scanInfo

        scanInfoService.saveLog(containerId, log)

        assertThat(scanInfo.logs.map { it.logMessage }).contains(log)
        verify { scanInfoRepository.save(scanInfo) }
    }

    @Test
    fun testSaveLogShouldThrowEntityNotFoundExceptionWhenNotExistingIdPassed() {
        val containerId = "dummyContainerId"
        val log = "dummy log"

        every { scanInfoRepository.findByContainerId(containerId) } returns Optional.empty()

        assertThrows<EntityNotFoundException> {
            scanInfoService.saveLog(containerId, log)
        }
    }

    @Test
    fun testCreateScanInfoShouldSaveScanInfo() {
        val containerId = "dummyContainerId"
        val domain = "dummy.com"
        val scanInfo = ScanInfo(containerId = containerId, domain = domain)

        every { scanInfoRepository.save(any()) } returns scanInfo

        val result = scanInfoService.createScanInfo(containerId, domain)

        assertThat(containerId).isEqualTo(result.containerId)
        assertThat(domain).isEqualTo(result.domain)
        verify { scanInfoRepository.save(any()) }
    }

    @Test
    fun testUpdateStatusByContainerIdShouldUpdateStatus() {
        val containerId = "dummyContainerId"
        val domain = "dummy.com"
        val scanInfo = ScanInfo(containerId = containerId, domain = domain)
        every { scanInfoRepository.findByContainerId(containerId) } returns Optional.of(scanInfo)
        every { scanInfoRepository.save(scanInfo) } returns scanInfo

        val updatedScan = scanInfoService.updateStatusByContainerId(containerId, ScanStatus.SUCCESS)

        assertThat(ScanStatus.SUCCESS).isEqualTo(updatedScan.status)
        assertThat(updatedScan.endTime).isNotNull()
        verify { scanInfoRepository.save(scanInfo) }
    }

    @Test
    fun testUpdateStatusByContainerIdShouldNotUpdateIfScanIsTerminal() {
        val containerId = "dummyContainerId"
        val domain = "dummy.com"
        val scanInfo = ScanInfo(containerId = containerId, domain = domain, status = ScanStatus.SUCCESS)

        every { scanInfoRepository.findByContainerId(containerId) } returns Optional.of(scanInfo)

        val updatedScan = scanInfoService.updateStatusByContainerId(containerId, ScanStatus.PROCESSING)

        assertThat(ScanStatus.SUCCESS).isEqualTo(updatedScan.status)
        verify(exactly = 0) { scanInfoRepository.save(any()) }
    }

    @Test
    fun testUpdateStatusByContainerIdShouldThrowExceptionWhenScanInfoNotFound() {
        val containerId = "dummyContainerId"

        every { scanInfoRepository.findByContainerId(containerId) } returns Optional.empty()

        assertThrows<EntityNotFoundException> {
            scanInfoService.updateStatusByContainerId(containerId, ScanStatus.SUCCESS)
        }
    }

    @Test
    fun testFindByScanIdShouldReturnScanInfoIfExists() {
        val scanId = 1L
        val containerId = "dummyContainerId"
        val domain = "dummy.com"
        val scanInfo = ScanInfo(id = scanId, containerId = containerId, domain = domain)

        every { scanInfoRepository.findById(scanId) } returns Optional.of(scanInfo)

        val result = scanInfoService.findByScanId(scanId)

        assertThat(scanId).isEqualTo(result.id)
        verify { scanInfoRepository.findById(scanId) }
    }

    @Test
    fun testFindByScanIdShouldThrowExceptionIfNotFound() {
        val scanId = 1L

        every { scanInfoRepository.findById(scanId) } returns Optional.empty()

        assertThrows<EntityNotFoundException> {
            scanInfoService.findByScanId(scanId)
        }
    }

    @Test
    fun testFindScansInProcessingStateShouldReturnOnlyScansInProcessingState() {
        val scanInfo1 = ScanInfo(
            containerId = "1",
            domain = "a.com",
            status = ScanStatus.PROCESSING
        )

        every { scanInfoRepository.findByStatus(ScanStatus.PROCESSING) } returns listOf(scanInfo1)

        val result = scanInfoService.findScansInProcessingState()

        assertThat(listOf(scanInfo1)).isEqualTo(result)
        verify { scanInfoRepository.findByStatus(ScanStatus.PROCESSING) }
    }
}