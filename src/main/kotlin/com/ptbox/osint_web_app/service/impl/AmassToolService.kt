package com.ptbox.osint_web_app.service.impl

import com.ptbox.osint_web_app.client.docker.DockerClient
import com.ptbox.osint_web_app.configuration.AmassProperties
import com.ptbox.osint_web_app.service.ScanInfoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AmassToolService(
    private val dockerClient: DockerClient,
    private val amassProperties: AmassProperties,
    private val scanInfoService: ScanInfoService,
) : OsintToolLifecycleService(scanInfoService) {
    private val logger = LoggerFactory.getLogger(AmassToolService::class.java)

    override fun initScanning(domain: String): String {
        if (!dockerClient.imageExists(amassProperties.getImageIdentifier())) {
            logger.info("Image ${amassProperties.getImageIdentifier()} not exist. Initialize pulling.")
            dockerClient.pullImage(amassProperties.getImageIdentifier())
        }

        logger.info("Run container...")
        val containerId = dockerClient.runContainer(
            image = amassProperties.getImageIdentifier(),
            args = listOf(SUBDOMAIN_SCAN, IN_DOMAIN, domain),
        )
        return containerId
    }

    override fun processScanLogs(containerId: String) {
        try {
            logger.info("Init logs observing for container $containerId")
            dockerClient.streamContainerLogs(containerId) { log ->
                scanInfoService.saveLog(containerId, log)
            }

            val exitCode = dockerClient.waitForContainer(containerId)

            if (exitCode == 0) {
                onSuccess(containerId)
            }
        } catch (e: Exception) {
            logger.error("Error processing scan logs for container $containerId", e)
            onFailure(containerId, e.message ?: "Unknown error")
        }
    }

    override fun stopScanning(containerId: String) {
        if (dockerClient.isContainerAlive(containerId)) {
            dockerClient.stopContainer(containerId)
        }
        onTerminated(containerId)
    }

    companion object {
        const val SUBDOMAIN_SCAN = "enum"
        const val IN_DOMAIN = "-d"
    }
}