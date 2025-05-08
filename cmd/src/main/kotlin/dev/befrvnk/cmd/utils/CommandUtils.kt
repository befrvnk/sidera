package dev.befrvnk.cmd.utils

import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio

fun executeCommand(commandParts: List<String>): String {
    val child = Command(commandParts.first())
        .args(commandParts.drop(1))
        .stdout(Stdio.Pipe)
        .spawn()
    val lines = child.bufferedStdout()?.lines()?.joinToString("\n")
    child.wait()
    return lines ?: ""
}

fun loadSecret(
    secretName: String,
    environmentFilePath: String,
): String {
    val commandParts = listOf(
        "op", "run", "--no-masking", "--env-file=$environmentFilePath",
        "--",
        "printenv", secretName,
    )
    // Consider adding error handling here based on the output or exit code
    return executeCommand(commandParts).trim()
}
