plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.shared.feature.licenses"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.ui.common)

                implementation(libs.compose.components.resources)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)

                implementation(libs.aboutlibraries.compose.m3)
                implementation(libs.aboutlibraries.core)
                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
            }
        }
    }
}
