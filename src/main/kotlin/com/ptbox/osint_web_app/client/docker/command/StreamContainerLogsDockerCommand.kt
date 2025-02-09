package com.ptbox.osint_web_app.client.docker.command

import com.ptbox.osint_web_app.client.docker.util.StreamLogsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StreamContainerLogsDockerCommand(
    private val containerId: String,
    private val onLogReceived: (String) -> Unit
) : DockerCommand<Unit> {

    override fun execute() {
        val process = ProcessBuilder("docker", "logs", "-f", containerId).start()

        val job = CoroutineScope(Dispatchers.IO).launch {
            process.inputStream.bufferedReader().useLines { lines ->
                lines.forEach { onLogReceived(it) }
            }
        }

        StreamLogsManager.register(containerId, process, job)
    }
}