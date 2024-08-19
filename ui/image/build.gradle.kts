plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.ui.image"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(libs.coil)
            implementation(libs.coil.compose)
            implementation(libs.koin.core)
            implementation(projects.shared.domain)
            implementation(projects.ui.core)
        }
        }
        val androidMain by getting {
            dependencies {
            implementation(compose.preview)
            implementation(libs.coil.network.okhttp)
        }
        }
        val jvmMain by getting {
            dependencies {
            implementation(libs.coil.network.okhttp)
        }
        }
        val iosMain by getting {
            dependencies {
            implementation(libs.coil.network.ktor2)
        }
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
