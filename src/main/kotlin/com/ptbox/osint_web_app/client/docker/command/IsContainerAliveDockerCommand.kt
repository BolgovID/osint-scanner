package com.ptbox.osint_web_app.client.docker.command

import com.ptbox.osint_web_app.client.docker.util.DockerExecutor
import org.slf4j.LoggerFactory

class IsContainerAliveDockerCommand(
    private val containerId: String
) : DockerCommand<Boolean> {
    private val logger = LoggerFactory.getLogger(IsContainerAliveDockerCommand::class.java)

    override fun execute(): Boolean {
        val isAlive = DockerExecutor.runCommand(
            listOf("docker", "inspect", "--format", "{{.State.Running}}", containerId)
        ).toBooleanStrictOrNull() ?: false

        if (isAlive) {
            logger.debug("Container $containerId is running right now")
        } else {
            logger.debug("Container $containerId is not running right now")
        }
        return isAlive
    }
}