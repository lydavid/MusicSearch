plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
    alias(libs.plugins.paparazzi)
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.shared.feature.stats"
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

                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
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
                implementation(libs.test.parameter.injector)
                implementation(libs.junit)
                implementation(libs.circuit.test)
                implementation(libs.robolectric)
                implementation(libs.koin.test)
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
