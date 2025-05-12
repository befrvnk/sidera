package dev.befrvnk.cmd.app.keystore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.check
import kotlinx.coroutines.runBlocking
import kotlin.io.encoding.ExperimentalEncodingApi

// Creates a keystore file from 1Password
class KeystoreCreateCommand : CliktCommand(name = "create") {
    val name: String by argument(
        name = "project",
        help = "Project name of the keystore to create",
    ).check { it.all { char -> char.isLowerCase() } }

    override fun help(context: Context) = "Creates a keystore file from a base64 string stored in 1Password."

    @OptIn(ExperimentalEncodingApi::class)
    override fun run() = runBlocking {
        KeystoreTask.create(name)
    }
}