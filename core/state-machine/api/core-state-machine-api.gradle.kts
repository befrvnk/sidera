plugins {
    alias(libs.plugins.fgp.core.android)
}

freeletics {
}

dependencies {
    api(libs.androidx.viewmodel.savedstate)
    api(libs.flowredux)
    api(libs.khonshu.statemachine)
    api(libs.kotlinx.coroutines)

    implementation(libs.timber)
}