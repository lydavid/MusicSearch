plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.musicsearch.compose.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.android.feature.nowplaying"
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.domain)
    implementation(projects.shared.strings)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    testImplementation(projects.ui.test.screenshot)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)

    implementation(libs.circuit.foundation)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.paging.compose)
    implementation(libs.paging.common)

    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.test.parameter.injector)
}
