package dev.befrvnk.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import dev.befrvnk.cmd.api.apiCommand
import dev.befrvnk.cmd.app.appCommand
import dev.befrvnk.cmd.keystore.keystoreCommand
import dev.befrvnk.cmd.module.ModuleCommand

class SideraCommand : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    SideraCommand()
        .subcommands(
            apiCommand,
            appCommand,
            keystoreCommand,
            ModuleCommand(),
        ).main(args)
}
