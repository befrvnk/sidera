plugins {
    alias(libs.plugins.fgp.core.android).apply(false)
    alias(libs.plugins.fgp.core.kotlin).apply(false)
    alias(libs.plugins.fgp.domain.android).apply(false)
    alias(libs.plugins.fgp.domain.kotlin).apply(false)
    alias(libs.plugins.fgp.legacy.android).apply(false)
    alias(libs.plugins.fgp.feature).apply(false)
    alias(libs.plugins.fgp.nav).apply(false)
    alias(libs.plugins.fgp.app).apply(false)
    alias(libs.plugins.fgp.jvm).apply(false)
    alias(libs.plugins.android).apply(false)
    alias(libs.plugins.kotest.multiplatform).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.play).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.dependency.analysis).apply(false)
    alias(libs.plugins.shadow).apply(false)

    alias(libs.plugins.fgp.root)
}

// Workaround for jib tasks in api projects
buildscript {
    dependencies {
        classpath(libs.commons.compress)
    }
}
