plugins {
    application
    alias(libs.plugins.fgp.jvm)
    alias(libs.plugins.shadow)
}

application {
    mainClass.set("dev.befrvnk.cmd.MainKt")
}

dependencies {
    implementation(libs.clikt)
    implementation(libs.kommand)
    implementation(libs.kotlinx.io)

    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.framework.engine)
}

val generatedSourcesDirProvider = project.layout.buildDirectory.dir("generated/sources/templates/main/kotlin")
val templateDirProvider = project.layout.projectDirectory.dir("src/main/resources/templates")
val generatedTemplatesFileProvider = generatedSourcesDirProvider.map { it.file("GeneratedTemplates.kt") }
val generatedPackageName = "dev.befrvnk.cmd.generated"

kotlin {
    sourceSets.main {
        kotlin.srcDir(generatedSourcesDirProvider)
    }
}

val generateTemplates = tasks.register<GenerateTemplatesTask>("generateTemplatesKt") {
    description = "Generates a Kotlin source file containing template file contents."
    group = "build"

    templateDirectory.set(templateDirProvider)
    generatedFile.set(generatedTemplatesFileProvider)
    outputPackageName.set(generatedPackageName)
}

// Ensure the generation task runs before compilation
tasks.compileKotlin {
    dependsOn(generateTemplates)
}

tasks.named<Delete>("clean") {
    delete(generatedSourcesDirProvider)
}
