plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.settings"

    buildTypes {
        all {
            buildConfigField(
                type = "int",
                name = "VERSION_CODE",
                value = project.properties["VERSION_CODE"] as String? ?: "",
            )
            buildConfigField(
                type = "String",
                name = "VERSION_NAME",
                value = "\"${project.properties["VERSION_NAME"] as String? ?: ""}\"",
            )
        }
    }
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.core.preferences)
    implementation(projects.data.musicbrainz) // TODO: remove after extracting MusicBrainzAuthStore
    implementation(projects.domain)
    implementation(projects.strings)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(libs.aboutlibraries.compose)
    implementation(libs.aboutlibraries.core)
    implementation(libs.appauth)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)

    debugImplementation(libs.compose.ui.tooling)

    ksp(libs.koin.ksp.compiler)

    testRuntimeOnly(libs.bundles.kotlinx.coroutines)

    testImplementation(projects.ui.test.screenshot)
    testImplementation(libs.test.parameter.injector)
}
