plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.build.config)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.settings"
}

buildConfig {
    buildConfigField(
        name = "VERSION_CODE",
        value = project.properties["VERSION_CODE"] as String? ?: "",
    )
    buildConfigField(
        name = "VERSION_NAME",
        value = project.properties["VERSION_NAME"] as String? ?: "",
    )
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            // TODO: remove after extracting MusicBrainzAuthStore
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(libs.circuit.foundation)
            implementation(libs.koin.core)
            implementation(projects.core.preferences)
            implementation(projects.data.musicbrainz)
            implementation(projects.shared.domain)
            implementation(projects.ui.common)
        }
        }
        val androidMain by getting {
            dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.appauth)
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
    implementation(compose.preview)

    debugImplementation(compose.uiTooling)
}
