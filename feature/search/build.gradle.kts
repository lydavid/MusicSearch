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
    implementation(projects.data) // TODO: remove dependency
    implementation(projects.domain) // TODO: remove dependency
    implementation(projects.data.database) // TODO: remove dependency
    implementation(projects.strings)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    testImplementation(projects.ui.test.screenshot)

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.test.parameter.injector)
}
