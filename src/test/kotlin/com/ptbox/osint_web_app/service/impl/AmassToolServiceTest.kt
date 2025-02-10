package com.ptbox.osint_web_app.service.impl

import com.ptbox.osint_web_app.client.docker.DockerClient
import com.ptbox.osint_web_app.configuration.AmassProperties
import com.ptbox.osint_web_app.service.ScanInfoService
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AmassToolServiceTest {
    private val dockerClient: DockerClient = mockk(relaxed = true)
    private val amassProperties: AmassProperties = mockk(relaxed = true)
    private val scanInfoService: ScanInfoService = mockk(relaxed = true)
    private lateinit var amassToolService: AmassToolService


    @BeforeEach
    fun setUp() {
        amassToolService = AmassToolService(dockerClient, amassProperties, scanInfoService)
    }

    @Test
    fun testInitScanningShouldInitializeScanningAndReturnContainerId() {
        val domain = "dummy.com"
        val imageId = "dummy-image"
        val containerId = "dummyContainer"


        every { amassProperties.getImageIdentifier() } returns imageId
        every { dockerClient.imageExists(imageId) } returns false
        every { dockerClient.pullImage(imageId) } just Runs
        every { dockerClient.runContainer(imageId, any()) } returns containerId

        val result = amassToolService.initScanning(domain)

        verify { dockerClient.pullImage(imageId) }
        verify { dockerClient.runContainer(imageId, listOf("enum", "-d", domain)) }
        assertThat(containerId).isEqualTo(result)
    }

    @Test
    fun testProcessScanLogsShouldProcessScanLogsAndHandleSuccess() {
        val containerId = "dummyContainer"

        every { dockerClient.streamContainerLogs(containerId, any()) } just Runs
        every { dockerClient.waitForContainer(containerId) } returns 0
        every { scanInfoService.saveLog(containerId, any()) } just Runs

        amassToolService.processScanLogs(containerId)

        verify { dockerClient.streamContainerLogs(containerId, any()) }
        verify { dockerClient.waitForContainer(containerId) }
    }

    @Test
    fun testStopContainerShouldStopScanningIfContainerIsAlive() {
        val containerId = "dummyContainer"

        every { dockerClient.isContainerAlive(containerId) } returns true
        every { dockerClient.stopContainer(containerId) } just Runs

        amassToolService.stopScanning(containerId)

        verify { dockerClient.stopContainer(containerId) }
    }
}