plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
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
                implementation(projects.core.models)
                implementation(projects.core.preferences)
                implementation(projects.data.musicbrainz) // TODO: remove after extracting MusicBrainzAuthStore
                implementation(projects.domain)
                implementation(projects.shared.screens)
                implementation(projects.strings)
                implementation(projects.ui.common)
                implementation(projects.ui.core)

                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.koin.annotations)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.appauth)

                implementation(libs.androidx.activity.compose)
                implementation(libs.koin.androidx.compose)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.screenshot)
                implementation(libs.test.parameter.injector)
                implementation(libs.bundles.kotlinx.coroutines)
            }
        }
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}