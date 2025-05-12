package dev.befrvnk.cmd.app.keystore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.check

// Deletes keystore file after publish task
class KeystoreDeleteCommand : CliktCommand(name = "delete") {
    val project: String by argument(
        name = "project",
        help = "Project name of the keystore to delete",
    ).check { it.all { char -> char.isLowerCase() } }

    override fun help(context: Context) = "Deletes a created keystore file from the project."

    override fun run() {
        KeystoreTask.delete(project)
    }
}