plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            implementation(libs.koin.core)
            implementation(projects.data.common.network)
            implementation(projects.data.coverart)
            implementation(projects.data.musicbrainz)
            implementation(projects.data.spotify)
            implementation(projects.shared.domain)
        }
        }
    }
}
