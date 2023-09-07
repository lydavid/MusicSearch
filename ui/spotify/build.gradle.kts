@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
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
    implementation(projects.data.core)
    implementation(projects.data.spotify)
    implementation(projects.ui.core)
    implementation(projects.ui.common)
    testImplementation(projects.ui.test.screenshot)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.test.parameter.injector)
}
