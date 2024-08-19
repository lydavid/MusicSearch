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
    implementation(libs.circuit.foundation)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.paging.common)
    implementation(libs.paging.compose)
    implementation(projects.shared.domain)
    implementation(projects.ui.common)

    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.test.parameter.injector)
    testImplementation(projects.ui.test.screenshot)
}
