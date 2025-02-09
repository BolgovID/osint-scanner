package com.ptbox.osint_web_app.client.docker

import com.ptbox.osint_web_app.client.docker.command.*
import org.springframework.stereotype.Component

@Component
class LocalDockerClient : DockerClient {

    override fun imageExists(image: String) = ImageExistsDockerCommand(image).execute()

    override fun pullImage(image: String) = PullImageDockerCommand(image).execute()
    override fun waitForContainer(containerId: String): Int = WaitDockerCommand(containerId).execute()

    override fun runContainer(image: String, args: List<String>) = RunDockerCommand(image, args).execute()
    override fun streamContainerLogs(containerId: String, onLogReceived: (String) -> Unit) =
        StreamContainerLogsDockerCommand(containerId, onLogReceived).execute()

    override fun stopContainer(containerId: String) = StopContainerDockerCommand(containerId).execute()

    override fun isContainerAlive(containerId: String) = IsContainerAliveDockerCommand(containerId).execute()

}