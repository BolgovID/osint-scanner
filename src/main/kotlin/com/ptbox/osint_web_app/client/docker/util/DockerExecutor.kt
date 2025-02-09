package com.ptbox.osint_web_app.client.docker.util

import com.ptbox.osint_web_app.client.docker.exception.DockerExecutionException
import org.slf4j.LoggerFactory

object DockerExecutor {
    private val logger = LoggerFactory.getLogger(DockerExecutor::class.java)

    fun runCommand(commands: List<String>): String {
        logger.debug("Executing docker command: {}", commands)
        val process = ProcessBuilder(commands).start()
        if (process.waitFor() != 0) {
            logger.error("Error executing docker command: {}", commands)
            throw DockerExecutionException("Command failed: $commands")
        }
        return process.inputStream.bufferedReader()
            .readText()
            .trim()
    }
}