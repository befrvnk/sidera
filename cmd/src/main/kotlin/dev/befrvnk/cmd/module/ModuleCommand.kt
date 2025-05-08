package dev.befrvnk.cmd.module

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

class ModuleCommand : CliktCommand(name = "module") {
    val gradlePath: String by argument(
        name = "module path",
        help = "Gradle path of the new module, e.g. ':feature:home'.",
    )

    override fun help(context: Context): String = "Creates a new module based on the given Gradle path."

    override fun run() {
        val variables = mutableMapOf (
            "module_path" to modulePath(gradlePath),
            "file_base_name" to fileBaseName(gradlePath),
            "gradle_file_name" to gradleFileName(gradlePath),
            "package_name" to packageName(gradlePath),
            "package_path" to packagePath(packageName = packageName(gradlePath)),
            "module_accessor" to moduleAccessor(gradlePath),
        )

        // core, domain, feature
        val moduleType = gradlePath
            .trimStart(':')
            .split(":")
            .first()
            .split("-")
            .first()
        val modules = when (moduleType) {
            "core" -> {
                print("Kotlin or Android module [kotlin]: ")
                val platformType = readlnOrNull()?.ifEmpty { "kotlin" } ?: "kotlin"

                print("Package name [${variables["package_name"]}]: ")
                val packageName = readlnOrNull()?.ifEmpty { variables["package_name"]!! } ?: variables["package_name"]!!
                variables["package_name"] = packageName
                variables["package_path"] = packagePath(packageName)

                when (platformType) {
                    "kotlin" -> listOf(
                        ModuleTemplates.CORE_KOTLIN_API,
                        ModuleTemplates.CORE_KOTLIN_IMPLEMENTATION,
                    )
                    "android" -> listOf(
                        ModuleTemplates.CORE_ANDROID_API,
                        ModuleTemplates.CORE_ANDROID_IMPLEMENTATION,
                    )
                    else -> throw Exception("Unknown platform type: $platformType")
                }
            }
            "domain" -> {
                print("Kotlin or Android module [kotlin]: ")
                val platformType = readlnOrNull()?.ifEmpty { "kotlin" } ?: "kotlin"

                print("Package name [${variables["package_name"]}]: ")
                val packageName = readlnOrNull()?.ifEmpty { variables["package_name"]!! } ?: variables["package_name"]!!
                variables["package_name"] = packageName
                variables["package_path"] = packagePath(packageName)

                when (platformType) {
                    "kotlin" -> listOf(
                        ModuleTemplates.DOMAIN_KOTLIN_API,
                        ModuleTemplates.DOMAIN_KOTLIN_IMPLEMENTATION,
                    )
                    "android" -> listOf(
                        ModuleTemplates.DOMAIN_ANDROID_API,
                        ModuleTemplates.DOMAIN_ANDROID_IMPLEMENTATION,
                    )
                    else -> throw Exception("Unknown platform type: $platformType")
                }
            }
            "feature" -> listOf(
                ModuleTemplates.FEATURE_IMPLEMENTATION,
                ModuleTemplates.FEATURE_NAV,
            )
            else -> throw Exception("Unknown module type: $moduleType")
        }

        modules.forEach { module ->
            module.templates.forEach { template ->
                when (template) {
                    is Template.Directory -> {
                        println("Creating directories ${template.path.replaceVariables(variables)}")
                        SystemFileSystem.createDirectories(Path(template.path.replaceVariables(variables)))
                    }
                    is Template.File -> {
                        println("Writing file ${template.path.replaceVariables(variables)}")
                        SystemFileSystem.sink(Path(template.path.replaceVariables(variables)))
                            .buffered()
                            .use { sink -> sink.writeString(template.content.replaceVariables(variables)) }
                    }
                }
            }
        }
    }
}

/**
 * Replaces placeholders within the string instance with corresponding values from the provided map.
 * Placeholders are denoted by curly braces `{}` and the key inside them is used to find the replacement
 * in the map. For example, `{key}` will be replaced with the value associated with "key" in the map.
 *
 * @param variables a map where keys represent placeholder names (without `{}`)
 *                  and values represent the corresponding replacement strings.
 * @return a new string where all placeholders (if found in the map) are replaced with their values.
 *         If a placeholder does not have a corresponding entry in the map, it remains unchanged.
 */
fun String.replaceVariables(variables: Map<String, String>): String = variables
    .entries
    .fold(this) { acc, entry -> acc.replace("{${entry.key}}", entry.value) }
