package dev.befrvnk.cmd.module

sealed class Template {
    data class Directory(
        val path: String,
    ) : Template()

    data class File(
        val path: String,
        val content: String,
    ) : Template()
}