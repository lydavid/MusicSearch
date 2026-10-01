plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
    alias(libs.plugins.build.config)
    alias(libs.plugins.paparazzi)
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.shared.feature.details"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.ui.common)
                implementation(projects.shared.domain)
                implementation(projects.core.logging.api)

                implementation(libs.compose.components.resources)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.circuit.overlay)
                implementation(libs.circuit.retained)
                implementation(libs.circuitx.overlays)
                implementation(libs.koin.core)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
                implementation(libs.kotlinx.collections.immutable)
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
                implementation(projects.testData)
                implementation(projects.ui.test.screenshot)
                implementation(libs.test.parameter.injector)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.junit)
                implementation(libs.koin.test)
                implementation(libs.circuit.test)
                implementation(libs.robolectric)
                implementation(libs.mockk)
            }
        }
    }
}
