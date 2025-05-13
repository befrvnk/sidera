package dev.befrvnk.cmd.api.dev

import dev.befrvnk.cmd.utils.executeCommand
import dev.befrvnk.cmd.utils.output
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

object DevTask {
    fun up(project: String) = runBlocking {
        executeCommand(
            listOf("podman", "compose", "-f", "api/$project/api-$project.compose.yml", "up", "-d")
        ).collect()
        println("Development environment started.")
    }

    fun down(project: String) = runBlocking {
        executeCommand(
            listOf("podman", "compose", "-f", "api/$project/api-$project.compose.yml", "down")
        ).collect()
        println("Development environment stopped.")
    }

    fun ps(project: String) = runBlocking {
        executeCommand(
            listOf("podman", "ps", "-f", "name=$project")
        ).output()
    }
}
