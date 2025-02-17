plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.domain)
                implementation(projects.data.musicbrainz)
                implementation(projects.data.coverart)
                implementation(projects.data.spotify)
                implementation(projects.data.database)
                implementation(libs.koin.core)
                implementation(libs.koin.test)
                implementation(projects.core.coroutines)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.junit)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
    }
}
