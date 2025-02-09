package com.ptbox.osint_web_app.client.docker.command

import com.ptbox.osint_web_app.client.docker.util.DockerExecutor

class ImageExistsDockerCommand(
    private val imageName: String
) : DockerCommand<Boolean> {
    override fun execute(): Boolean {
        return DockerExecutor.runCommand(listOf("docker", "images", "-q", imageName))
            .toBooleanStrictOrNull() ?: false

    }
}