plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.musicsearch.compose.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.test.image"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.test)
}
