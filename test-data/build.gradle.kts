plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.data.common.network)
                implementation(projects.data.musicbrainz)
                implementation(projects.data.coverart)
                implementation(projects.data.spotify)
                implementation(projects.shared.domain)
                implementation(libs.koin.core)
            }
        }
    }
}
