plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.android.feature.nowplaying"
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.domain)
    implementation(projects.strings)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    testImplementation(projects.ui.test.screenshot)

    implementation(libs.paging.compose)
    implementation(libs.paging.common)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.circuit.foundation)
    implementation(libs.koin.core)

    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.test.parameter.injector)
}
