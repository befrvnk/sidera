package dev.befrvnk.cmd.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class ApiRegistryCommand : CliktCommand(name = "registry") {
    val project: String by argument(
        name = "project",
        help = "Name of the project to deploy",
    ).validate {
        // The folder ./api/projectName must exist
        require(SystemFileSystem.exists(Path("api/$it"))) {
            "Project folder does not exist: ./api/$it"
        }
    }

    override fun help(context: Context): String = "Publishes Docker image registry for the given project."

    override fun run() = runBlocking {
        ApiTask.registry(project)
    }
}