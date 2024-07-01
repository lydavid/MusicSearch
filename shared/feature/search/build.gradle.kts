plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.feature.search"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.shared.domain)
                implementation(projects.shared.strings)
                implementation(projects.ui.common)
                implementation(projects.ui.core)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
            }
        }
        val androidMain by getting
        val jvmMain by getting
    }
}

dependencies {
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
}
