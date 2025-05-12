package dev.befrvnk.cmd.keystore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import dev.befrvnk.cmd.utils.loadSecret
import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// Creates a keystore file from 1Password
class KeystoreCreateCommand : CliktCommand(name = "create") {
    val name: String by option(
        "-n", "--name",
        help = "Name of the keystore to create",
    ).required().check { it.all { char -> char.isLowerCase() } }

    override fun help(context: Context) = "Creates a keystore file from a base64 string stored in 1Password."

    @OptIn(ExperimentalEncodingApi::class)
    override fun run() = runBlocking {
        val keystoreFileName = "$name.jks"
        val keystoreBase64 = loadSecret("KEY_STORE_FILE_ENCODED", environmentFilePath)
        val keystoreBytes = Base64.Default.decode(keystoreBase64)
        val keystoreSink = SystemFileSystem.sink(Path(keystoreFileName))
        keystoreSink.buffered().write(keystoreBytes)
        echo("Created keystore file: $keystoreFileName")
    }
}