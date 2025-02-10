package com.ptbox.osint_web_app.client.docker.command

import com.ptbox.osint_web_app.client.docker.util.DockerExecutor
import org.slf4j.LoggerFactory

class WaitDockerCommand(
    private val containerId: String
) : DockerCommand<Int> {
    private val logger = LoggerFactory.getLogger(WaitDockerCommand::class.java)

    override fun execute(): Int {
        try {
            val exitCode = DockerExecutor.runCommand(listOf("docker", "wait", containerId))
                .toIntOrNull() ?: -1
            logger.debug("Container $containerId exited with code $exitCode")
            return exitCode
        } catch (e: Exception) {
            logger.warn("Docker daemon: ${e.message}")
            return -1
        }

    }
}