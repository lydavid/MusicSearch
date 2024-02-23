plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ly.david.musicsearch.feature.details"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.domain)
                implementation(projects.ui.common)
                implementation(projects.ui.core)
                implementation(projects.strings)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.collections.immutable)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.screenshot)
                implementation(libs.test.parameter.injector)
            }
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
