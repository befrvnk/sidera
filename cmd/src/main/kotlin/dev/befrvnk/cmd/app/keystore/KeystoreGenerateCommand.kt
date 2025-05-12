package dev.befrvnk.cmd.app.keystore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.check
import kotlinx.coroutines.runBlocking
import kotlin.io.encoding.ExperimentalEncodingApi

class KeystoreGenerateCommand : CliktCommand(name = "generate") {
    val project: String by argument(
        name = "project",
        help = "Project name of the keystore to generate",
    ).check { it.all { char -> char.isLowerCase() } }

    override fun help(context: Context) =
        "Generates a keystore file for the project. Outputs the base64 encoded file, alias, and password."

    @OptIn(ExperimentalEncodingApi::class)
    override fun run() = runBlocking {
        KeystoreTask.generate(project)
    }
}