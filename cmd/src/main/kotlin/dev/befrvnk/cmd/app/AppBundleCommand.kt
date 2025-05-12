package dev.befrvnk.cmd.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import dev.befrvnk.cmd.utils.ProcessEvent
import dev.befrvnk.cmd.utils.executeCommand
import dev.befrvnk.cmd.utils.loadSecret
import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class AppBundleCommand : CliktCommand(name = "bundle") {
    val projectName: String by option(
        "-n", "--project-name",
        help = "Name of the project to publish",
    ).required().validate {
        // The folder ./app/projectName must exist
        require(SystemFileSystem.exists(Path("app/$it"))) {
            "Project folder does not exist: ./app/$it"
        }
    }

    override fun help(context: Context): String = "Creates a release bundle for the given project."

    @OptIn(ExperimentalEncodingApi::class)
    override fun run() = runBlocking {
        val envFilePath = ".env/app_${projectName}.env"
        val gradleBundleRelease = "./gradlew :app:${projectName}:bundleRelease --rerun-tasks || " +
                "{ echo \"Gradle publish failed\" >&2; exit 1; }"

        val keystoreFileName = "$projectName.jks"
        val keystoreBase64 = loadSecret("KEY_STORE_FILE_ENCODED", envFilePath)
        val keystoreBytes = Base64.Default.decode(keystoreBase64)
        SystemFileSystem.sink(kotlinx.io.files.Path(keystoreFileName)).buffered().write(keystoreBytes)

        val storePass = loadSecret("ORG_GRADLE_PROJECT_fgpReleaseStorePassword", envFilePath)
        val keyAlias = loadSecret("ORG_GRADLE_PROJECT_fgpReleaseKeyAlias", envFilePath)

        echo("Project name: $projectName")
        echo("Environment file: $envFilePath")

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
                is ProcessEvent.OutputLine -> echo(it.line)
                else -> {}
            }
        }
    }
}