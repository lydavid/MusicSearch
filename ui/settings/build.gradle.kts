plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("kotlin-kapt")
    id("app.cash.paparazzi")
}

android {
    namespace = "ly.david.ui.settings"

    buildTypes {
        all {
            buildConfigField("int", "VERSION_CODE", project.properties["VERSION_CODE"] as String? ?: "")
            buildConfigField("String", "VERSION_NAME", "\"${project.properties["VERSION_NAME"] as String? ?: ""}\"")
        }
        release {
            isMinifyEnabled = true
            consumerProguardFiles("consumer-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.dataAndroid)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    testImplementation(projects.ui.test.screenshot)

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

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    testRuntimeOnly(libs.bundles.kotlinx.coroutines)

    testImplementation(libs.test.parameter.injector)
}
