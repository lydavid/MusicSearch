plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.ui.collection"
}

dependencies {
    implementation(projects.data)
    implementation(projects.dataAndroid)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(projects.ui.settings)
    testImplementation(projects.ui.test.screenshot)

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    testImplementation(libs.bundles.kotlinx.coroutines)

    testImplementation(libs.test.parameter.injector)
}
