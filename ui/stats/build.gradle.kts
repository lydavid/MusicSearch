plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.ui.stats"
}

dependencies {
    implementation(projects.data)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    implementation(libs.kotlinx.collections.immutable)

    debugImplementation(libs.compose.ui.tooling)

    testImplementation(projects.ui.test.screenshot)
    testImplementation(libs.test.parameter.injector)
}
