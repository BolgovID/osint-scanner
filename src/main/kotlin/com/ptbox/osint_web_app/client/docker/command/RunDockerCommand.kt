package com.ptbox.osint_web_app.client.docker.command

import com.ptbox.osint_web_app.client.docker.util.DockerExecutor

class RunDockerCommand(
    private val imageName: String,
    private val imageCommandArgs: List<String>
) : DockerCommand<String> {

    override fun execute(): String {
        val params = buildList {
            addAll(listOf("docker", "run", "-d", "--rm", imageName))
            addAll(imageCommandArgs)
        }
        return DockerExecutor.runCommand(params)
    }
}