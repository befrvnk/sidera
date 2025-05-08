package dev.befrvnk.cmd.module

import dev.befrvnk.cmd.generated.GeneratedTemplates

enum class ModuleTemplates(val templates: List<Template>) {
    CORE_ANDROID_API(
        listOf(
            Template.Directory(path = "{module_path}/api/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/api/{gradle_file_name}-api.gradle.kts",
                content = GeneratedTemplates.CoreAndroidApiBuildGradle,
            ),
        )
    ),
    CORE_ANDROID_IMPLEMENTATION(
        listOf(
            Template.Directory(path = "{module_path}/implementation/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/implementation/{gradle_file_name}-implementation.gradle.kts",
                content = GeneratedTemplates.CoreAndroidImplementationBuildGradle,
            )
        )
    ),
    CORE_KOTLIN_API(
        listOf(
            Template.Directory(path = "{module_path}/api/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/api/{gradle_file_name}-api.gradle.kts",
                content = GeneratedTemplates.CoreKotlinApiBuildGradle,
            ),
        )
    ),
    CORE_KOTLIN_IMPLEMENTATION(
        listOf(
            Template.Directory(path = "{module_path}/implementation/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/implementation/{gradle_file_name}-implementation.gradle.kts",
                content = GeneratedTemplates.CoreKotlinImplementationBuildGradle,
            ),
        )
    ),
    DOMAIN_ANDROID_API(
        listOf(
            Template.Directory(path = "{module_path}/api/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/api/{gradle_file_name}-api.gradle.kts",
                content = GeneratedTemplates.DomainAndroidApiBuildGradle,
            ),
        )
    ),
    DOMAIN_ANDROID_IMPLEMENTATION(
        listOf(
            Template.Directory(path = "{module_path}/implementation/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/implementation/{gradle_file_name}-implementation.gradle.kts",
                content = GeneratedTemplates.DomainAndroidImplementationBuildGradle,
            ),
        )
    ),
    DOMAIN_KOTLIN_API(
        listOf(
            Template.Directory(path = "{module_path}/api/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/api/{gradle_file_name}-api.gradle.kts",
                content = GeneratedTemplates.DomainKotlinApiBuildGradle,
            ),
        )
    ),
    DOMAIN_KOTLIN_IMPLEMENTATION(
        listOf(
            Template.Directory(path = "{module_path}/implementation/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/implementation/{gradle_file_name}-implementation.gradle.kts",
                content = GeneratedTemplates.DomainKotlinImplementationBuildGradle,
            ),
        )
    ),
    FEATURE_IMPLEMENTATION(
        listOf(
            Template.Directory(path = "{module_path}/implementation/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/implementation/src/main/kotlin/{package_path}/{file_base_name}Action.kt",
                content = GeneratedTemplates.FeatureImplementationAction,
            ),
            Template.File(
                path = "{module_path}/implementation/{gradle_file_name}-implementation.gradle.kts",
                content = GeneratedTemplates.FeatureImplementationBuildGradle,
            ),
            Template.File(
                path =  "{module_path}/implementation/src/main/kotlin/{package_path}/{file_base_name}Navigator.kt",
                content = GeneratedTemplates.FeatureImplementationNavigator,
            ),
            Template.File(
                path = "{module_path}/implementation/src/main/kotlin/{package_path}/{file_base_name}State.kt",
                content = GeneratedTemplates.FeatureImplementationState,
            ),
            Template.File(
                path = "{module_path}/implementation/src/main/kotlin/{package_path}/{file_base_name}StateMachine.kt",
                content = GeneratedTemplates.FeatureImplementationStateMachine,
            ),
            Template.File(
                path = "{module_path}/implementation/src/main/kotlin/{package_path}/{file_base_name}Ui.kt",
                content = GeneratedTemplates.FeatureImplementationUi,
            ),
        )
    ),
    FEATURE_NAV(
        listOf(
            Template.Directory(path = "{module_path}/nav/src/main/kotlin/{package_path}/nav/"),
            Template.File(
                path = "{module_path}/nav/{gradle_file_name}-nav.gradle.kts",
                content = GeneratedTemplates.FeatureNavBuildGradle,
            ),
            Template.File(
                path = "{module_path}/nav/src/main/kotlin/{package_path}/nav/{file_base_name}NavDirections.kt",
                content = GeneratedTemplates.FeatureNavDirections,
            )
        )
    )
}