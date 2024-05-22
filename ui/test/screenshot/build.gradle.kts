plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.musicsearch.compose.multiplatform")
}

android {
    namespace = "ly.david.ui.test.screenshot"
}

dependencies {
    implementation(projects.ui.core)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.paparazzi)
    implementation(libs.test.parameter.injector)
}
