plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.history"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.core.preferences)
                implementation(projects.shared.domain)
                implementation(projects.ui.common)
                implementation(projects.ui.core)
                implementation(projects.ui.image)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.image)
                implementation(projects.ui.test.screenshot)
                implementation(libs.test.parameter.injector)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.coil.compose)
            }
        }
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}
