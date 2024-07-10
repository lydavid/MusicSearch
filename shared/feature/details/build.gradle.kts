plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.build.config)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.details"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.core.preferences)
                implementation(projects.core.logging.api)
                implementation(projects.data.common.network)
                implementation(projects.data.spotify)
                implementation(projects.data.musicbrainz)
                implementation(projects.data.database)
                implementation(projects.ui.image)
                implementation(projects.shared.domain)
                implementation(projects.ui.common)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)

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
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(projects.testData)
                implementation(projects.ui.test.screenshot)
                implementation(libs.test.parameter.injector)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.junit)
                implementation(libs.koin.test)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(projects.testData)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.test.espresso.core)
                implementation(libs.androidx.arch.core.testing)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.compose.ui.test)
                implementation(libs.koin.test)
                implementation(libs.sqldelight.android.driver)
                implementation(libs.test.parameter.injector)
                implementation(libs.circuit.test)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
