plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.ui.settings"

    buildTypes {
        all {
            buildConfigField("int", "VERSION_CODE", project.properties["VERSION_CODE"] as String? ?: "")
            buildConfigField("String", "VERSION_NAME", "\"${project.properties["VERSION_NAME"] as String? ?: ""}\"")
        }
    }
}

dependencies {
    implementation(projects.dataAndroid)
    implementation(projects.domain)
    implementation(projects.ui.common)
    implementation(projects.ui.core)

    implementation(libs.aboutlibraries.compose)
    implementation(libs.aboutlibraries.core)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.appauth)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    testRuntimeOnly(libs.bundles.kotlinx.coroutines)

    testImplementation(projects.ui.test.screenshot)
    testImplementation(libs.test.parameter.injector)
}
