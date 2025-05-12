package dev.befrvnk.cmd.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.flag
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

class ApiPublishCommand : CliktCommand(name = "deploy") {
    val projectName: String by option(
        "-n", "--project-name",
        help = "Name of the project to deploy",
    ).required().validate {
        // The folder ./api/projectName must exist
        require(SystemFileSystem.exists(Path("api/$it"))) {
            "Project folder does not exist: ./api/$it"
        }
    }

    val verbose: Boolean by option("--verbose", "-v").flag()

    override fun help(context: Context): String = "Deploys latest project image to Fly."

    override fun run() = runBlocking {
        val flyApiToken = loadSecret("FLY_API_TOKEN", apiEnvFilePath)

        executeCommand(
            commandParts = listOf(
                "fly", "deploy",
                "--config", "api/$projectName/api-$projectName.fly.toml",
                "--access-token", flyApiToken,
            ),
        ).collect {
            when {
                verbose && it is ProcessEvent.OutputLine -> echo(it.line)
                else -> {}
            }
        }
    }
}
