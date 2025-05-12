package dev.befrvnk.cmd.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.subcommands
import dev.befrvnk.cmd.app.keystore.keystoreCommand

val appCommand = AppCommand().subcommands(
    AppBundleCommand(),
    keystoreCommand,
)

class AppCommand : CliktCommand() {
    override fun run() = Unit

    override fun help(context: Context): String = "Commands for bundling and publishing Android apps."
}