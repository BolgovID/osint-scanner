package com.ptbox.osint_web_app.bootstrap

import com.ptbox.osint_web_app.client.docker.DockerClient
import com.ptbox.osint_web_app.configuration.AmassProperties
import com.ptbox.osint_web_app.entity.ScanStatus
import com.ptbox.osint_web_app.service.ScanInfoService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class BootstrapService(
    private val scanInfoService: ScanInfoService,
    private val dockerClient: DockerClient,
    private val amassProperties: AmassProperties,
) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(BootstrapService::class.java)

    override fun run(vararg args: String?) {
        recoverFailedContainers()
        prepareOsintTool()
    }

    private fun prepareOsintTool() {
        val imageName = amassProperties.getImageIdentifier()
        if (!dockerClient.imageExists(imageName)) {
            logger.info("Image $imageName not exist. Initiate pulling")
            dockerClient.pullImage(imageName)
        }
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