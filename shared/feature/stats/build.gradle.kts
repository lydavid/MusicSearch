plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.stats"
}

kotlin {
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
            }
        }
        val androidUnitTest by getting {
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
