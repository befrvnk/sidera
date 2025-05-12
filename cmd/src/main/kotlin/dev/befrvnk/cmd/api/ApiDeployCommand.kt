package dev.befrvnk.cmd.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class ApiPublishCommand : CliktCommand(name = "deploy") {
    val project: String by argument(
        name = "project",
        help = "Name of the project to deploy",
    ).validate {
        // The folder ./api/projectName must exist
        require(SystemFileSystem.exists(Path("api/$it"))) {
            "Project folder does not exist: ./api/$it"
        }
    }

    val verbose: Boolean by option("--verbose", "-v").flag()

    override fun help(context: Context): String = "Deploys latest project image to Fly."

    override fun run() = runBlocking {
        ApiTask.deploy(project, verbose)
    }
}
