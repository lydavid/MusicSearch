plugins {
    id("ly.david.android.library")
    id("kotlin-kapt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "ly.david.data"
}

dependencies {
    api(projects.data)

    api(libs.coil.base)
    api(libs.coil.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kaptTest(libs.hilt.android.compiler)
    implementation(libs.hilt.android.testing)
    implementation(libs.room.runtime)
}
