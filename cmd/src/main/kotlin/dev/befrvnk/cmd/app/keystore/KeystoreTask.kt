package dev.befrvnk.cmd.app.keystore

import dev.befrvnk.cmd.utils.executeCommand
import dev.befrvnk.cmd.utils.generateSecurePassword
import dev.befrvnk.cmd.utils.loadSecret
import kotlinx.coroutines.flow.collect
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private const val environmentFilePath = ".env/keystore_distinguished_name.env"

internal object KeystoreTask {
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun create(name: String) {
        val keystoreFileName = "$name.jks"
        val keystoreBase64 = loadSecret("KEY_STORE_FILE_ENCODED", environmentFilePath)
        val keystoreBytes = Base64.Default.decode(keystoreBase64)
        val keystoreSink = SystemFileSystem.sink(Path(keystoreFileName))
        keystoreSink.buffered().write(keystoreBytes)
        println("Created keystore file: $keystoreFileName")
    }

    fun delete(name: String) {
        val keystoreFileName = "$name.jks"
        SystemFileSystem.delete(
            path = Path(keystoreFileName),
            mustExist = false,
        )
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun generate(name: String) {
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

        println(
            "Generated keystore for $name. Please save these values into your 1Password vault and create" +
                    " a environment file in .env for the following values:"
        )
        println("Alias: $name")
        println("Store pass: $storePass")
        println("Keystore file base64:")
        println(base64String)

        // Delete the keystore file after base64 conversion
        SystemFileSystem.delete(
            path = kotlinx.io.files.Path(keystoreFileName),
            mustExist = false,
        )
    }
}