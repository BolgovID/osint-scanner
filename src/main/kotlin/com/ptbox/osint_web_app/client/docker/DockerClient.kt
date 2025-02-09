package com.ptbox.osint_web_app.client.docker

interface DockerClient {
    fun runContainer(image: String, args: List<String>): String
    fun streamContainerLogs(containerId: String, onLogReceived: (String) -> Unit)
    fun imageExists(image: String): Boolean
    fun pullImage(image: String)
    fun waitForContainer(containerId: String): Int
    fun stopContainer(containerId: String)
    fun isContainerAlive(containerId: String): Boolean
}