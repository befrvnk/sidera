package dev.befrvnk.cmd.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.io.encoding.ExperimentalEncodingApi

class AppBundleCommand : CliktCommand(name = "bundle") {
    val project: String by argument(
        name = "project",
        help = "Name of the project to publish",
    ).validate {
        // The folder ./api/projectName must exist
        require(SystemFileSystem.exists(Path("api/$it"))) {
            "Project folder does not exist: ./api/$it"
        }
    }

    override fun help(context: Context): String = "Creates a release bundle for the given project."

    @OptIn(ExperimentalEncodingApi::class)
    override fun run() = runBlocking {
        AppTask.bundle(project)
    }
}