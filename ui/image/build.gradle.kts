plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    alias(libs.plugins.ksp)
}

android {
    namespace = "ly.david.ui.image"
}

dependencies {
    implementation(projects.data.core)
    implementation(projects.ui.core)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.coil.base)
    implementation(libs.coil.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}
