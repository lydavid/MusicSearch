plugins {
    id("ly.david.android.library")
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
                implementation(projects.feature.search)
                implementation(projects.feature.stats)
                implementation(projects.strings)
                implementation(projects.ui.common)
                implementation(projects.ui.history)
                implementation(libs.koin.core)
                implementation(libs.circuit.foundation)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.android.feature.nowplaying)
                implementation(projects.android.feature.spotify)
                implementation(projects.ui.commonLegacy)
                implementation(projects.ui.core)
                implementation(projects.ui.collections)
                implementation(projects.ui.image)
                implementation(projects.ui.settings)
            }
        }
        val jvmMain by getting
    }
}
