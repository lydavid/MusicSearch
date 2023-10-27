plugins {
    id("ly.david.android.library")
    kotlin("android")
    id("ly.david.android.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.ui.common"
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.data.coverart)
    implementation(projects.data.musicbrainz)
    implementation(projects.domain)
    implementation(projects.strings)
    implementation(projects.ui.core)
    implementation(projects.ui.image)

    implementation(libs.accompanist.swiperefresh)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.paging.common)
    implementation(libs.paging.compose)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.coil.base)
    implementation(libs.coil.compose)

    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    implementation(libs.lyricist.library)

    implementation(libs.timber)

    testImplementation(projects.ui.test.image)
    testImplementation(projects.ui.test.screenshot)
    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.coil.compose)
    testImplementation(libs.coil.test)
    testImplementation(libs.test.parameter.injector)
    testImplementation(libs.koin.test)
}
