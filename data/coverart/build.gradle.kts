plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.domain)
                implementation(projects.core.logging.api)
                implementation(libs.koin.core)
                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.paging.common)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(projects.testData)
                implementation(projects.data.musicbrainz)
                implementation(projects.data.database)
                implementation(libs.kotlin.test)
                implementation(libs.koin.test)
                implementation(libs.junit)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.androidx.paging.testing)
            }
        }
    }
}
