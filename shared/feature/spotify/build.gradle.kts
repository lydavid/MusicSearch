plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
    alias(libs.plugins.paparazzi)
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.shared.feature.spotify"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.ui.common)
                implementation(projects.shared.domain)

                implementation(libs.compose.components.resources)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.compose.ui.tooling)
            }
        }
        val androidHostTest by getting {
            dependencies {
                implementation(projects.ui.test.screenshot)
            }
        }
    }
}
