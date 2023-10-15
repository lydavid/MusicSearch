plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.ui.history"
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.core.preferences)
    implementation(projects.domain)
    implementation(projects.invertedDomain)
    implementation(projects.strings)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(projects.ui.image)
    testImplementation(projects.ui.test.image)
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

    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.coil.compose)
    testImplementation(libs.test.parameter.injector)
}
