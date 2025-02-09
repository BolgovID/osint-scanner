package com.ptbox.osint_web_app.service

import com.ptbox.osint_web_app.client.docker.DockerClient
import com.ptbox.osint_web_app.entity.ScanStatus
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class ScanRecoveryService(
    private val scanInfoService: ScanInfoService,
    private val dockerClient: DockerClient
) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(ScanRecoveryService::class.java)

    override fun run(vararg args: String?) {
        recoverFailedContainers()
    }

    private fun recoverFailedContainers() {
        val containersInProcessing = scanInfoService.findScansInProcessingState()

        containersInProcessing.forEach {
            if (dockerClient.isContainerAlive(it.containerId)) {
                logger.info("Found alive container with id: ${it.containerId}")
                dockerClient.stopContainer(it.containerId)
            }
            scanInfoService.updateStatusByContainerId(it.containerId, ScanStatus.FAILED)
        }
    }
}