package dev.befrvnk.cmd.app

import dev.befrvnk.cmd.utils.ProcessEvent
import dev.befrvnk.cmd.utils.executeCommand
import dev.befrvnk.cmd.utils.loadSecret
import kotlinx.io.buffered
import kotlinx.io.files.SystemFileSystem
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal object AppTask {
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun bundle(
        projectName: String,
    ) {
        val envFilePath = ".env/app_${projectName}.env"
        val gradleBundleRelease = "./gradlew :app:${projectName}:bundleRelease --rerun-tasks || " +
                "{ echo \"Gradle publish failed\" >&2; exit 1; }"

        val keystoreFileName = "$projectName.jks"
        val keystoreBase64 = loadSecret("KEY_STORE_FILE_ENCODED", envFilePath)
        val keystoreBytes = Base64.Default.decode(keystoreBase64)
        SystemFileSystem.sink(kotlinx.io.files.Path(keystoreFileName)).buffered().write(keystoreBytes)

        val storePass = loadSecret("ORG_GRADLE_PROJECT_fgpReleaseStorePassword", envFilePath)
        val keyAlias = loadSecret("ORG_GRADLE_PROJECT_fgpReleaseKeyAlias", envFilePath)

        println("Project name: $projectName")
        println("Environment file: $envFilePath")

        // Construct the command list for ProcessBuilder
        val command = listOf(
            "sh", "-c", """
            export ORG_GRADLE_PROJECT_fgpReleaseStorePassword="$storePass"
            export ORG_GRADLE_PROJECT_fgpReleaseKeyAlias="$keyAlias"
            export ORG_GRADLE_PROJECT_fgpReleaseKeyPassword="$storePass"
            export ORG_GRADLE_PROJECT_fgpReleaseStoreFile="$keystoreFileName"
            $gradleBundleRelease
        """.trimIndent()
        )

        executeCommand(command).collect {
            when (it) {
                is ProcessEvent.OutputLine -> println(it.line)
                else -> {}
            }
        }
    }
}