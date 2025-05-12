package dev.befrvnk.cmd.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import dev.befrvnk.cmd.utils.ProcessEvent
import dev.befrvnk.cmd.utils.executeCommand
import dev.befrvnk.cmd.utils.loadSecret
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

private const val apiEnvFilePath = ".env/api.env"

class ApiRegistryCommand : CliktCommand(name = "registry") {
    val projectName: String by option(
        "-n", "--project-name",
        help = "Name of the project to publish registry image",
    ).required().validate {
        // The folder ./api/projectName must exist
        require(SystemFileSystem.exists(Path("api/$it"))) {
            "Project folder does not exist: ./api/$it"
        }
    }

    override fun help(context: Context): String = "Publishes Docker image registry for the given project."

    override fun run() = runBlocking {
        val dockerUsername = loadSecret("DOCKER_USERNAME", apiEnvFilePath)
        val dockerPassword = loadSecret("DOCKER_PASSWORD", apiEnvFilePath)

        executeCommand(
            listOf(
                "./gradlew", ":api:$projectName:jib",
                "-Djib.to.auth.username=$dockerUsername",
                "-Djib.to.auth.password=$dockerPassword",
            )
        ).collect {
            when (it) {
                is ProcessEvent.OutputLine -> echo(it.line)
                else -> {}
            }
        }
    }
}