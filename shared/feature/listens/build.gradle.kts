plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.listens"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.ui.common)
                implementation(projects.shared.domain)

                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)

                implementation(libs.circuit.foundation)
                implementation(libs.circuit.overlay)
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
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.testData)
                implementation(projects.ui.test.screenshot)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.junit)
                implementation(libs.circuit.test)
                implementation(libs.robolectric)
                implementation(libs.androidx.paging.testing)
            }
        }
    }
}
