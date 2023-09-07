plugins {
    id("ly.david.android.library")
    kotlin("android")
}

android {
    namespace = "ly.david.ui.test.image"
}

dependencies {
    implementation(libs.coil.base)
    implementation(libs.bundles.kotlinx.coroutines)
}
