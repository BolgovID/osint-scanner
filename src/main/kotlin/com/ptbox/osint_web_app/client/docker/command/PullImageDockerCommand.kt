package com.ptbox.osint_web_app.client.docker.command

import com.ptbox.osint_web_app.client.docker.util.DockerExecutor
import org.slf4j.LoggerFactory

class PullImageDockerCommand(
    private val imageName: String
) : DockerCommand<Unit> {
    private val logger = LoggerFactory.getLogger(PullImageDockerCommand::class.java)

    override fun execute() {
        val output = DockerExecutor.runCommand(listOf("docker", "pull", imageName))
        if (output.isNotEmpty()) {
            logger.debug("Image pulled successfully: $imageName")
        }
    }
}