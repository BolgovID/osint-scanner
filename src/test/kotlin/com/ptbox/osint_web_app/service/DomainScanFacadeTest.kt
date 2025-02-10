package com.ptbox.osint_web_app.service

import com.ptbox.osint_web_app.entity.ScanInfo
import com.ptbox.osint_web_app.entity.ScanStatus
import com.ptbox.osint_web_app.exception.DomainAlreadyScanningException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.Instant
import kotlin.test.assertFailsWith

class DomainScanFacadeTest {
    private lateinit var domainScanFacade: DomainScanFacade
    private val osintJobService: OsintJobService = mock(OsintJobService::class.java)
    private val scanInfoService: ScanInfoService = mock(ScanInfoService::class.java)

    @BeforeEach
    fun setUp() {
        domainScanFacade = DomainScanFacade(osintJobService, scanInfoService)
    }

    @Test
    fun testScanDomainShouldStartScanningWhenDomainIsNotBeingScanned() {
        val domain = "example.com"
        val containerId = "containerId"
        val scanInfo = generateDummyScan(domain)

        `when`(scanInfoService.findScansInProcessingState()).thenReturn(emptyList())
        `when`(osintJobService.initScanning(domain)).thenReturn(containerId)
        `when`(scanInfoService.createScanInfo(containerId, domain)).thenReturn(scanInfo)

        domainScanFacade.scanDomain(domain)

        verify(osintJobService).initScanning(domain)
        verify(scanInfoService).createScanInfo(containerId, domain)
    }

    @Test
    fun testScanDomainShouldThrowExceptionWhenDomainIsAlreadyBeingScanned() {
        val domain = "example.com"
        val dummyScan = generateDummyScan(domain)

        `when`(scanInfoService.findScansInProcessingState()).thenReturn(listOf(dummyScan))

        assertFailsWith<DomainAlreadyScanningException> {
            domainScanFacade.scanDomain(domain)
        }
    }

    @Test
    fun testStopScanningShouldStopScanningSuccessfully() {
        val scanId = 1L
        val dummyScan = generateDummyScan("example.com")
        `when`(scanInfoService.findByScanId(scanId)).thenReturn(dummyScan)

        val result = domainScanFacade.stopScanning(scanId)

        verify(osintJobService).stopScanning(dummyScan.containerId)
        assertThat(dummyScan.id).isEqualTo(result.id)
    }

    @Test
    fun testFindAllScansShouldReturnAllScans() {
        val scanInfo1 = generateDummyScan("example1.com")
        val scanInfo2 = generateDummyScan("example2.com")

        `when`(scanInfoService.findAll()).thenReturn(listOf(scanInfo1, scanInfo2))

        val result = domainScanFacade.findAllScans()

        assertThat(result.size).isEqualTo(2)
    }

    private fun generateDummyScan(domain: String): ScanInfo {
        return ScanInfo(
            id = 0,
            containerId = "123",
            domain = domain,
            endTime = Instant.now(),
            status = ScanStatus.PROCESSING
        )
    }
}