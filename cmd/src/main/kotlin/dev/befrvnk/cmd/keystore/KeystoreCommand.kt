package dev.befrvnk.cmd.keystore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.subcommands

internal const val environmentFilePath = ".env/keystore_distinguished_name.env"

val keystoreCommand = KeystoreCommand().subcommands(
    KeystoreGenerateCommand(),
    KeystoreCreateCommand(),
    KeystoreDeleteCommand()
)

class KeystoreCommand : CliktCommand() {
    override fun run() = Unit

    override fun help(context: Context): String = "Commands to generate, create, and delete a keystore file."
}
