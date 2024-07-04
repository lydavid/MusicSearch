plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.images"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.domain)
                implementation(projects.ui.image)
                implementation(projects.ui.common)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.screenshot)
                implementation(libs.junit)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
