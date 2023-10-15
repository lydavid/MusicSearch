plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.ui.spotify"
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.data.core)
    implementation(projects.ui.core)
    implementation(projects.ui.common)
    implementation(projects.strings)
    implementation(libs.androidx.core)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)

    debugImplementation(libs.compose.ui.tooling)

    testImplementation(projects.ui.test.screenshot)
    testImplementation(libs.test.parameter.injector)
}
