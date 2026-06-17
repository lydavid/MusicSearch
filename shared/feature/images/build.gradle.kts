plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
    alias(libs.plugins.paparazzi)
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.shared.feature.images"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.domain)
                implementation(projects.ui.common)

                implementation(libs.compose.components.resources)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.windowSizeClass)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
                implementation(libs.compose.ui.backhandler)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.test.image)

                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.compose.ui.tooling)
            }
        }
        val androidHostTest by getting {
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
