plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.ui.collection"
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.core.preferences)
    implementation(projects.data.database) // TODO: remove dep
    implementation(projects.data.musicbrainz) // TODO: remove dep
    implementation(projects.domain)
    implementation(projects.invertedDomain)
    implementation(projects.strings)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
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

    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.bundles.kotlinx.coroutines)

    testImplementation(libs.test.parameter.injector)
}
