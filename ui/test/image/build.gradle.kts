plugins {
    id("ly.david.android.library")
    kotlin("android")
}

android {
    namespace = "ly.david.musicsearch.ui.test.image"
}

dependencies {
    implementation(libs.coil.base)
    api(libs.coil.test)
    implementation(libs.bundles.kotlinx.coroutines)
}
