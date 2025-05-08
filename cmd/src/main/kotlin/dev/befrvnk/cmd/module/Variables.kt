package dev.befrvnk.cmd.module

fun modulePath(gradlePath: String): String = gradlePath
    .trimStart(':')
    .replace(":", "/")

// :feature-sorted:app-main -> AppMain
fun fileBaseName(gradlePath: String): String = gradlePath
    .split(":")
    .last()
    .split("-")
    .joinToString("") { it.replaceFirstChar { char -> char.uppercase() } }

// :feature-sorted:app-main -> feature-sorted-app-main.gradle.kts
fun gradleFileName(gradlePath: String): String = gradlePath
    .trimStart(':')
    .replace(":", "-")

// :feature-sorted:app-main -> dev.befrvnk.feature.sorted.appmain
fun packageName(gradlePath: String): String = "dev.befrvnk" + gradlePath
    .replace(":", ".")
    .replaceFirst("-", ".") // feature-sorted -> feature.sorted
    .replace("-", "")

// :feature-sorted:app-main -> dev/befrvnk/feature/sorted/appmain
fun packagePath(packageName: String): String = packageName.replace(".", "/")

// :feature-sorted:app-main -> featureSorted.appMain
fun moduleAccessor(gradlePath: String): String = gradlePath
    .trimStart(':')
    .split(":")
    .joinToString(".") { folder ->
        folder.split("-")
            .mapIndexed { index, item ->
                if (index != 0) {
                    item.replaceFirstChar { it.uppercase() }
                } else item
            }
            .joinToString("")
    }
