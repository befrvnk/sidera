package dev.befrvnk.cmd.module

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

internal object ModuleTask {
    fun generate(gradlePath: String) {
        val templateData = mutableMapOf (
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
            "api" -> {
                templateData.addData(key = "application_name", name = "Application Name")
                templateData.addData(key = "full_name", name = "Full Name")
                templateData.addData(key = "email", name = "Email")
                listOf(ModuleTemplates.API)
            }
            "app" -> {
                templateData.addData(key = "app_label", name = "App Label")
                listOf(ModuleTemplates.APP)
            }
            "core" -> {
                templateData.addData(
                    key = "platform_type",
                    name = "Platform type (android/kotlin)",
                    defaultValue = "kotlin",
                )
                templateData.addData(key = "package_name", name = "Package Name")
                templateData["package_path"] = packagePath(templateData["package_name"]!!)

                when (templateData["platform_type"]) {
                    "android" -> listOf(
                        ModuleTemplates.CORE_ANDROID_API,
                        ModuleTemplates.CORE_ANDROID_IMPLEMENTATION,
                    )
                    "kotlin" -> listOf(
                        ModuleTemplates.CORE_KOTLIN_API,
                        ModuleTemplates.CORE_KOTLIN_IMPLEMENTATION,
                    )
                    else -> throw Exception("Unknown platform type: ${templateData["platform_type"]}")
                }
            }
            "domain" -> {
                templateData.addData(
                    key = "platform_type",
                    name = "Platform type (android/kotlin)",
                    defaultValue = "kotlin",
                )
                templateData.addData(key = "package_name", name = "Package Name")
                templateData["package_path"] = packagePath(templateData["package_name"]!!)

                when (templateData["platform_type"]) {
                    "kotlin" -> listOf(
                        ModuleTemplates.DOMAIN_KOTLIN_API,
                        ModuleTemplates.DOMAIN_KOTLIN_IMPLEMENTATION,
                    )
                    "android" -> listOf(
                        ModuleTemplates.DOMAIN_ANDROID_API,
                        ModuleTemplates.DOMAIN_ANDROID_IMPLEMENTATION,
                    )
                    else -> throw Exception("Unknown platform type: ${templateData["platform_type"]}")
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
                        println("Creating directories ${template.path.replaceVariables(templateData)}")
                        SystemFileSystem.createDirectories(Path(template.path.replaceVariables(templateData)))
                    }
                    is Template.File -> {
                        println("Writing file ${template.path.replaceVariables(templateData)}")
                        SystemFileSystem.sink(Path(template.path.replaceVariables(templateData)))
                            .buffered()
                            .use { sink -> sink.writeString(template.content.replaceVariables(templateData)) }
                    }
                }
            }
        }
    }
}

private fun MutableMap<String, String>.addData(
    key: String,
    name: String,
    defaultValue: String? = null,
) {
    val currentOrDefault = if (defaultValue != null) {
        " [default: $defaultValue]"
    } else if (this.contains(key)) " [current: ${this[key]}]" else ""
    print("$name$currentOrDefault: ")
    val value = readlnOrNull()?.ifEmpty { defaultValue ?: this[key] } ?: throw Exception("$name is required")
    this[key] = value
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
private fun String.replaceVariables(variables: Map<String, String>): String = variables
    .entries
    .fold(this) { acc, entry -> acc.replace("{${entry.key}}", entry.value) }
