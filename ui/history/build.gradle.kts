plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("kotlin-kapt")
    id("app.cash.paparazzi")
}

android {
    namespace = "ly.david.ui.history"
}

dependencies {
    implementation(projects.data)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(projects.ui.image)
    testImplementation(projects.testData) // TODO: only used for FakeImageLoader
    testImplementation(projects.ui.test.screenshot)

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    testImplementation(libs.bundles.kotlinx.coroutines)

    testImplementation(libs.test.parameter.injector)
}
