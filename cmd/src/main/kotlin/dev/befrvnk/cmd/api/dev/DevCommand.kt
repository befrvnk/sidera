package dev.befrvnk.cmd.api.dev

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.obj
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

val devCommand = DevCommand().subcommands(
    DevUpCommand(),
    DevDownCommand(),
    DevPsCommand(),
)

class DevCommand : CliktCommand(name = "dev") {
    val project: String by argument(
        name = "project",
        help = "Name of the project to deploy",
    ).validate {
        // The folder ./api/projectName must exist
        require(SystemFileSystem.exists(Path("api/$it"))) {
            "Project folder does not exist: ./api/$it"
        }
    }

    override fun run() {
        currentContext.obj = project
    }

    override fun help(context: Context): String = "Commands for development of the given project."
}

class DevUpCommand : CliktCommand(name = "up") {
    override fun help(context: Context): String = "Starts the development environment for the given project."

    override fun run() {
        DevTask.up(currentContext.obj as String)
    }
}

class DevDownCommand : CliktCommand(name = "down") {
    override fun help(context: Context): String = "Stops the development environment for the given project."

    override fun run() {
        DevTask.down(currentContext.obj as String)
    }
}

class DevPsCommand : CliktCommand(name = "ps") {
    override fun help(context: Context): String =
        "Prints the status of the development environment for the given project."

    override fun run() {
        DevTask.ps(currentContext.obj as String)
    }
}
