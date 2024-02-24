plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ly.david.ui.history"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.core.parcelize)
                implementation(projects.core.preferences)
                implementation(projects.domain)
                implementation(projects.strings)
                implementation(projects.ui.common)
                implementation(projects.ui.core)
                implementation(projects.ui.image)
                implementation(projects.shared.screens)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
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
