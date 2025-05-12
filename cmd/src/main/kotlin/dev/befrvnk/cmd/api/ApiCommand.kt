package dev.befrvnk.cmd.api

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.subcommands

val apiCommand = ApiCommand().subcommands(
    ApiPublishCommand(),
    ApiRegistryCommand(),
)

class ApiCommand : CliktCommand(name = "api") {
    override fun run() = Unit

    override fun help(context: Context): String = "Commands for bundling and publishing JVM backend applications."
}