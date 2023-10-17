plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.feature.search"
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.domain)
    implementation(projects.strings)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)

    debugImplementation(libs.compose.ui.tooling)

    ksp(libs.koin.ksp.compiler)

    testImplementation(projects.ui.test.screenshot)
    testImplementation(libs.test.parameter.injector)
}
