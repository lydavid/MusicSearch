plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.build.config)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.collection"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.overlay)
            implementation(libs.circuitx.overlays)
            implementation(libs.koin.core)
            implementation(libs.paging.common)
            implementation(libs.paging.compose)
            implementation(projects.core.preferences)
            implementation(projects.shared.domain)
            implementation(projects.ui.common)
        }
        }
        val androidMain by getting {
            dependencies {
            implementation(compose.preview)
        }
        }

        val androidUnitTest by getting {
            dependencies {
            implementation(libs.bundles.kotlinx.coroutines)
            implementation(libs.test.parameter.injector)
            implementation(projects.ui.test.screenshot)
        }
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
