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
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(libs.circuit.foundation)
            implementation(libs.koin.core)
            implementation(libs.windowSizeClass)
            implementation(projects.shared.domain)
            implementation(projects.ui.common)
            implementation(projects.ui.image)
        }
        }
        val androidMain by getting {
            dependencies {
            implementation(compose.preview)
            implementation(projects.test.image)
        }
        }

        val androidUnitTest by getting {
            dependencies {
            implementation(libs.junit)
            implementation(projects.ui.test.screenshot)
        }
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
