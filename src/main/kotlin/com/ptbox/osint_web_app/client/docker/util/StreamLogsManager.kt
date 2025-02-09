package com.ptbox.osint_web_app.client.docker.util

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

data class LoggingListeningJob(val process: Process, val coroutineJob: Job)

object StreamLogsManager {
    private val logStreams = ConcurrentHashMap<String, LoggingListeningJob>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun register(containerId: String, process: Process, job: Job) {
        logStreams[containerId] = LoggingListeningJob(process, job)
    }

    fun stop(containerId: String) {
        logStreams.remove(containerId)?.let { listeningJob ->
            listeningJob.process.destroy()
            coroutineScope.launch {
                listeningJob.coroutineJob.cancelAndJoin()
            }
        }
    }
}