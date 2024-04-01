plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
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
                implementation(projects.core.models)
                implementation(projects.ui.common)
                implementation(projects.domain)
                implementation(projects.data.database)
                implementation(projects.ui.core)
                implementation(projects.strings)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
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
