plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.musicsearch.compose.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.ui.test.screenshot"
}

dependencies {
    implementation(projects.test.image)

    implementation(libs.coil.compose)
    implementation(libs.coil.test)
    implementation(libs.bundles.kotlinx.coroutines)

    implementation(libs.compose.components.resources)
    implementation(libs.compose.runtime)

    implementation(libs.paparazzi)
    implementation(libs.test.parameter.injector)
}
