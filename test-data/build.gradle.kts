plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.domain)
                implementation(projects.core.coroutines)
                implementation(projects.data.database)
                implementation(projects.data.musicbrainz)
                implementation(projects.data.coverart)
                implementation(projects.data.spotify)

                implementation(libs.koin.test)
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
