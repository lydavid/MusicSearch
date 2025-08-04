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

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)

                implementation(libs.aboutlibraries.compose.m3)
                implementation(libs.aboutlibraries.core)
                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
            }
        }
    }
}
