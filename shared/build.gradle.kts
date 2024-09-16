import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

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
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.overlay)
            implementation(libs.circuitx.gesture.navigation)
            implementation(libs.koin.core)
            implementation(libs.windowSizeClass)
            implementation(projects.core.coroutines)
            implementation(projects.core.logging.implementation)
            implementation(projects.core.preferences)
            implementation(projects.data.common.network)
            implementation(projects.data.coverart)
            implementation(projects.data.database)
            implementation(projects.data.musicbrainz)
            implementation(projects.data.repository)
            implementation(projects.data.spotify)
            implementation(projects.data.wikimedia)
            implementation(projects.shared.domain)
            implementation(projects.shared.feature.collections)
            implementation(projects.shared.feature.details)
            implementation(projects.shared.feature.graph)
            implementation(projects.shared.feature.history)
            implementation(projects.shared.feature.images)
            implementation(projects.shared.feature.licenses)
            implementation(projects.shared.feature.search)
            implementation(projects.shared.feature.settings)
            implementation(projects.shared.feature.stats)
            implementation(projects.ui.common)
            implementation(projects.ui.image)
        }
        }
        val androidMain by getting {
            dependencies {
            implementation(compose.preview)
            implementation(projects.android.feature.nowplaying)
            implementation(projects.android.feature.spotify)
            implementation(projects.ui.core)
        }
        }

        targets.withType<KotlinNativeTarget>().configureEach {
            binaries.framework {
                baseName = "shared"
                isStatic = true
            }
            binaries.configureEach {
                // https://github.com/touchlab/SQLiter/issues/77
                linkerOpts("-lsqlite3")
            }
        }
    }
}
