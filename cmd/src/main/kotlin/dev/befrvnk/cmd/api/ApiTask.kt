package dev.befrvnk.cmd.api

import dev.befrvnk.cmd.utils.ProcessEvent
import dev.befrvnk.cmd.utils.executeCommand
import dev.befrvnk.cmd.utils.loadSecret

private const val apiEnvFilePath = ".env/api.env"

internal object ApiTask {
    /**
     * Publishes a Docker image to a registry for a given project.
     *
     * @param projectName The name of the project whose Docker image should be published to a registry.
     */
    suspend fun registry(projectName: String) {
        val dockerUsername = loadSecret("DOCKER_USERNAME", apiEnvFilePath)
        val dockerPassword = loadSecret("DOCKER_PASSWORD", apiEnvFilePath)

        executeCommand(
            listOf(
                "./gradlew", ":api:$projectName:jib",
                "-Djib.to.auth.username=$dockerUsername",
                "-Djib.to.auth.password=$dockerPassword",
            )
        ).collect {
            when (it) {
                is ProcessEvent.OutputLine -> println(it.line)
                else -> {}
            }
        }
    }

    /**
     * Deploys a project using Fly CLI with a specified configuration file.
     *
     * @param project The name of the project to deploy, which determines
     *                    the configuration file path in the format `api/<projectName>/api-<projectName>.fly.toml`.
     * @param verbose A flag indicating whether to print detailed output during the deployment process.
     */
    suspend fun deploy(
        project: String,
        verbose: Boolean,
    ) {
        val flyApiToken = loadSecret("FLY_API_TOKEN", apiEnvFilePath)

        executeCommand(
            commandParts = listOf(
                "fly", "deploy",
                "--config", "api/$project/api-$project.fly.toml",
                "--access-token", flyApiToken,
            ),
        ).collect {
            when {
                verbose && it is ProcessEvent.OutputLine -> println(it.line)
                else -> {}
            }
        }
    }
}