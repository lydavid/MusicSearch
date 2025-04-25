plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.musicsearch.compose.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.android.feature.spotify"
}

dependencies {
    implementation(projects.ui.common)
    implementation(projects.shared.domain)
    implementation(libs.androidx.core)
    implementation(libs.circuit.foundation)
    implementation(libs.koin.core)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)

    implementation(libs.paging.compose)
    implementation(libs.paging.common)

    debugImplementation(libs.compose.ui.tooling)

    testImplementation(projects.ui.test.screenshot)
    testImplementation(libs.test.parameter.injector)
}
