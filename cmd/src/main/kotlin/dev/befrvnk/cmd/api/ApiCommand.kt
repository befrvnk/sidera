package dev.befrvnk.cmd.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.subcommands
import dev.befrvnk.cmd.api.dev.devCommand

val apiCommand = ApiCommand().subcommands(
    ApiPublishCommand(),
    ApiRegistryCommand(),
    devCommand,
)

class ApiCommand : CliktCommand(name = "api") {
    override fun run() = Unit

    override fun help(context: Context): String = "Commands for bundling and publishing JVM backend applications."
}