plugins {
    alias(libs.plugins.fgp.core.kotlin)
}

freeletics {
    useDagger()
}

dependencies {
    api(libs.dagger)
    api(libs.kotlinx.coroutines)
    api(projects.core.coroutines.api)
}