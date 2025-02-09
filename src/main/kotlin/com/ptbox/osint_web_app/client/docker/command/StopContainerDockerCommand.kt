package com.ptbox.osint_web_app.client.docker.command

import com.ptbox.osint_web_app.client.docker.util.DockerExecutor
import com.ptbox.osint_web_app.client.docker.util.StreamLogsManager

class StopContainerDockerCommand(
    private val containerId: String
) : DockerCommand<Unit> {
    override fun execute() {
        StreamLogsManager.stop(containerId)
        DockerExecutor.runCommand(listOf("docker", "stop", containerId))
    }
}