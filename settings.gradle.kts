pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.freeletics.gradle.settings").version("0.23.1")
}

rootProject.name = "sidera"

freeletics {
    discoverProjectsIn(true)
}
