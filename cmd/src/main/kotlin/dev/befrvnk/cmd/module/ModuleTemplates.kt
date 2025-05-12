package dev.befrvnk.cmd.module

import dev.befrvnk.cmd.generated.GeneratedTemplates

enum class ModuleTemplates(val templates: List<Template>) {
    API(
        listOf(
            Template.Directory(path = "{module_path}/src/main/kotlin/{package_path}/"),
            Template.Directory(path = "{module_path}/src/main/resources/static"),
            Template.File(
                path = "{module_path}/src/main/kotlin/{package_path}/{file_base_name}Application.kt",
                content = GeneratedTemplates.ApiApplication,
            ),
            Template.File(
                path = "{module_path}/src/main/resources/application.conf",
                content = GeneratedTemplates.ApiApplicationConf,
            ),
            Template.File(
                path = "{module_path}/{gradle_file_name}.gradle.kts",
                content = GeneratedTemplates.ApiBuildGradle,
            ),
            Template.File(
                path = "{module_path}/{gradle_file_name}.fly.toml",
                content = GeneratedTemplates.ApiFlyToml,
            ),
            Template.File(
                path = "{module_path}/src/main/resources/static/privacy-policy.html",
                content = GeneratedTemplates.ApiPrivacyPolicy,
            ),
            Template.File(
                path = "{module_path}/src/main/resources/static/terms-and-conditions.html",
                content = GeneratedTemplates.ApiTermsAndConditions,
            ),
        )
    ),
    APP(
        listOf(
            Template.Directory(path = "{module_path}/src/main/kotlin/{package_path}/"),
            Template.File(
                path = "{module_path}/src/main/kotlin/{package_path}/{file_base_name}App.kt",
                content = GeneratedTemplates.App,
            ),
            Template.File(
                path = "{module_path}/src/main/AndroidManifest.xml",
                content = GeneratedTemplates.AppAndroidManifest,
            ),
            Template.File(
                path = "{module_path}/{gradle_file_name}.gradle.kts",
                content = GeneratedTemplates.AppBuildGradle,
            ),
            Template.File(
                path = "{module_path}/src/main/kotlin/{package_path}/{file_base_name}AppComponent.kt",
                content = GeneratedTemplates.AppComponent,
            ),
            Template.File(
                path = "{module_path}/src/main/kotlin/{package_path}/{file_base_name}AppModule.kt",
                content = GeneratedTemplates.AppModule,
            ),
        )
    ),
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
                path = "{module_path}/implementation/src/main/kotlin/{package_path}/{file_base_name}Navigator.kt",
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