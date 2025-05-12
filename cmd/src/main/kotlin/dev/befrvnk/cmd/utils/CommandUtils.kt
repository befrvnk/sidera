package dev.befrvnk.cmd.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import java.util.concurrent.TimeUnit

sealed class ProcessEvent {
    data class OutputLine(val line: String) : ProcessEvent()
    data class Exit(val exitCode: Int) : ProcessEvent()
    data class Error(val message: String, val exception: Throwable? = null) : ProcessEvent()
}

/**
 * Executes a system command and streams its output and final exit code.
 *
 * @param commandParts A list where the first element is the command
 *                     and subsequent elements are its arguments.
 * @param timeout The maximum time to wait for the command to complete, in seconds.
 * @return A Flow emitting ProcessEvent.OutputLine for each line of output,
 *         and finally ProcessEvent.Exit with the exit code, or ProcessEvent.Error if an issue occurs.
 */
fun executeCommand(commandParts: List<String>, timeout: Long = 60): Flow<ProcessEvent> = callbackFlow {
    if (commandParts.isEmpty()) {
        trySend(ProcessEvent.Error("Command parts cannot be empty."))
        close()
        return@callbackFlow
    }

    val processBuilder = ProcessBuilder(commandParts)
    processBuilder.redirectErrorStream(true) // Merge stdout and stderr

    try {
        val process = processBuilder.start()

        // Coroutine to read output lines
        val outputJob = launch(Dispatchers.IO) { // Use IO dispatcher for blocking read
            try {
                process.inputStream.bufferedReader().use { reader ->
                    while (isActive) {
                        val lineRead = reader.readLine()
                        if (lineRead != null) {
                            send(ProcessEvent.OutputLine(lineRead))
                        } else {
                            // End of stream reached or an error occurred during readLine that results in null
                            break
                        }
                    }
                }
            } catch (e: IOException) {
                if (isActive) { // Only send an error if the flow is still active
                    send(ProcessEvent.Error("IOException while reading process output.", e))
                }
            }
        }

        // Wait for the process to complete with timeout
        val exited = process.waitFor(timeout, TimeUnit.SECONDS)

        outputJob.join() // Ensure all output is read before closing

        if (exited) {
            send(ProcessEvent.Exit(process.exitValue()))
            close()
        } else {
            process.destroyForcibly()
            send(ProcessEvent.Error("Command timed out after $timeout seconds."))
            close()
        }

    } catch (e: IOException) {
        send(ProcessEvent.Error("IOException while starting or waiting for command.", e))
        close()
    } catch (e: InterruptedException) {
        Thread.currentThread().interrupt() // Restore interrupted status
        send(ProcessEvent.Error("Command execution was interrupted.", e))
        close()
    }
}


suspend fun loadSecret(
    secretName: String,
    environmentFilePath: String,
): String {
    val commandParts = listOf(
        "op", "run", "--no-masking", "--env-file=$environmentFilePath",
        "--",
        "printenv", secretName,
    )
    // Consider adding error handling here based on the output or exit code
    return executeCommand(commandParts).first { event ->
        event is ProcessEvent.OutputLine
    }.let { event ->
        (event as ProcessEvent.OutputLine).line.trim()
    }
}
