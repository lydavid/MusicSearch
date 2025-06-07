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

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)

    implementation(libs.paparazzi)
    implementation(libs.test.parameter.injector)
}
