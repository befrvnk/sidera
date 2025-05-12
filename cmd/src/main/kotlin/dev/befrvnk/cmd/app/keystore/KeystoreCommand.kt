package dev.befrvnk.cmd.app.keystore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.subcommands

val keystoreCommand = KeystoreCommand().subcommands(
    KeystoreGenerateCommand(),
    KeystoreCreateCommand(),
    KeystoreDeleteCommand()
)

class KeystoreCommand : CliktCommand() {
    override fun run() = Unit

    override fun help(context: Context): String = "Commands to generate, create, and delete a keystore file."
}
