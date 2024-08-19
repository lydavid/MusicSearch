plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.musicsearch.compose.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.ui.test.screenshot"
}

dependencies {
    implementation(libs.bundles.kotlinx.coroutines)
    implementation(libs.coil.compose)
    implementation(libs.coil.test)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.paparazzi)
    implementation(libs.test.parameter.injector)
    implementation(projects.test.image)
    implementation(projects.ui.core)
}
