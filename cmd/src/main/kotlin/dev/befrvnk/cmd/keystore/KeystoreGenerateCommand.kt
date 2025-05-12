package dev.befrvnk.cmd.keystore

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import dev.befrvnk.cmd.utils.executeCommand
import dev.befrvnk.cmd.utils.generateSecurePassword
import dev.befrvnk.cmd.utils.loadSecret
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class KeystoreGenerateCommand : CliktCommand(name = "generate") {
    val name: String by option(
        "-n", "--name",
        help = "Name of the keystore to generate",
    ).required().check { it.all { char -> char.isLowerCase() } }

    override fun help(context: Context) =
        "Generates a keystore file for the project. Outputs the base64 encoded file, alias, and password."

    @OptIn(ExperimentalEncodingApi::class)
    override fun run() = runBlocking {
        val storePass = "pass:${generateSecurePassword(20)}"
        val keystoreFileName = "$name.jks"
        val commonName = loadSecret("COMMON_NAME", environmentFilePath)
        val locality = loadSecret("LOCALITY", environmentFilePath)
        val state = loadSecret("STATE", environmentFilePath)
        val countryCode = loadSecret("COUNTRY_CODE", environmentFilePath)

        // Build the command as a List<String>
        val commandParts = listOf(
            "keytool", "-genkeypair",
            "-v",
            "-keystore", keystoreFileName,
            "-alias", name,
            "-keyalg", "RSA",
            "-keysize", "4096",
            "-validity", "10000",
            "-storepass", storePass,
            "-dname", "CN=$commonName, L=$locality, ST=$state, C=$countryCode",
        )

        executeCommand(commandParts).collect()

        // Read the keystore file and convert to base64
        val keystoreSource = SystemFileSystem.source(Path(keystoreFileName))
        val keystoreBytes = keystoreSource.buffered().readByteArray()

        val base64String = Base64.Default.encode(keystoreBytes)

        echo(
            "Generated keystore for $name. Please save these values into your 1Password vault and create" +
                    " a environment file in .env for the following values:"
        )
        echo("Alias: $name")
        echo("Store pass: $storePass")
        echo("Keystore file base64:")
        echo(base64String)

        // Delete the keystore file after base64 conversion
        SystemFileSystem.delete(
            path = kotlinx.io.files.Path(keystoreFileName),
            mustExist = false,
        )
    }
}