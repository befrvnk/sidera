package dev.befrvnk.cmd.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import dev.befrvnk.cmd.utils.executeCommand
import dev.befrvnk.cmd.utils.loadSecret
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

private const val apiEnvFilePath = ".env/api.env"

class ApiPublishCommand : CliktCommand(name = "publish") {
    val projectName: String by option(
        "-n", "--project-name",
        help = "Name of the project to publish",
    ).required().validate {
        // The folder ./api/projectName must exist
        require(SystemFileSystem.exists(Path("api/$it"))) {
            "Project folder does not exist: ./api/$it"
        }
    }

    override fun help(context: Context): String = "Publishes an API project to Fly."

    override fun run() {
        val flyApiToken = loadSecret("FLY_API_TOKEN", apiEnvFilePath)

        executeCommand(
            listOf(
                "cd api/$projectName &&",
                "fly", "deploy",
                "--remote-only",
                "--access-token", flyApiToken,
            )
        )
    }
}
