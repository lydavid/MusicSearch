plugins {
    id("ly.david.android.library")
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
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(libs.circuit.foundation)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(projects.data.database)
            implementation(projects.shared.domain)
            implementation(projects.ui.common)
        }
        }
        val androidUnitTest by getting {
            dependencies {
            implementation(libs.test.parameter.injector)
            implementation(projects.ui.test.screenshot)
        }
        }
    }
}

dependencies {
    implementation(compose.preview)

    debugImplementation(compose.uiTooling)
}
