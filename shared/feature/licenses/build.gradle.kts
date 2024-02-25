plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.shared.feature.licenses"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.ui.common)
                implementation(projects.strings)
                implementation(projects.shared.screens)

                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation(libs.aboutlibraries.compose)
                implementation(libs.aboutlibraries.core)
                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
            }
        }
    }
}
