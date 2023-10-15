plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.data.core)
                implementation(projects.data.common.network)
                implementation(projects.data.musicbrainz)
                implementation(projects.data.coverart)
                implementation(projects.data.spotify)
                implementation(projects.invertedDomain)
                implementation(libs.koin.core)
            }
        }
    }
}
