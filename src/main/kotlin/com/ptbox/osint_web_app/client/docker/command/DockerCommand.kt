package com.ptbox.osint_web_app.client.docker.command

fun interface DockerCommand<T> {
    fun execute(): T
}