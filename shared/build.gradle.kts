plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.shared"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.coroutines)
                implementation(projects.core.logging.implementation)
                implementation(projects.core.models)
                implementation(projects.core.preferences)
                implementation(projects.data.common.network)
                implementation(projects.data.coverart)
                implementation(projects.data.database)
                implementation(projects.data.musicbrainz)
                implementation(projects.data.repository)
                implementation(projects.data.spotify)
                implementation(projects.domain)
                implementation(projects.shared.feature.collections)
                implementation(projects.shared.feature.history)
                implementation(projects.shared.feature.search)
                implementation(projects.shared.feature.settings)
                implementation(projects.shared.feature.licenses)
                implementation(projects.shared.feature.details)
                implementation(projects.feature.stats)
                implementation(projects.strings)
                implementation(projects.ui.common)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation(libs.koin.core)
                implementation(libs.circuit.foundation)
                implementation(libs.circuit.overlay)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.android.feature.nowplaying)
                implementation(projects.android.feature.spotify)
                implementation(projects.ui.commonLegacy)
                implementation(projects.ui.core)
                implementation(projects.ui.image)
            }
        }
        val jvmMain by getting
    }
}
