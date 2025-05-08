package dev.befrvnk.cmd.keystore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

// Deletes keystore file after publish task
class KeystoreDeleteCommand : CliktCommand(name = "delete") {
    val name: String by option(
        "-n", "--name",
        help = "Name of the keystore to delete",
    ).required().check { it.all { char -> char.isLowerCase() } }

    override fun help(context: Context) = "Deletes a created keystore file from the project."

    override fun run() {
        val keystoreFileName = "$name.jks"
        SystemFileSystem.delete(
            path = Path(keystoreFileName),
            mustExist = false,
        )
    }
}