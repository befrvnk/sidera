package dev.befrvnk.cmd.module

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument

class ModuleCommand : CliktCommand(name = "module") {
    val gradlePath: String by argument(
        name = "module path",
        help = "Gradle path of the new module, e.g. ':feature:home'.",
    )

    override fun help(context: Context): String = "Creates a new module based on the given Gradle path."

    override fun run() {
        ModuleTask.generate(gradlePath)
    }
}
