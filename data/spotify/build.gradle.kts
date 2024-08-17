import java.util.Properties

plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.build.config)
}

buildConfig {
    // These will only be set through GHA environment
    var spotifyClientId = project.properties["SPOTIFY_CLIENT_ID"] as String?
    var spotifyClientSecret = project.properties["SPOTIFY_CLIENT_SECRET"] as String?

    if (spotifyClientId.isNullOrEmpty() || spotifyClientSecret.isNullOrEmpty()) {
        val secretsPropertiesFile = rootProject.file("secrets.properties")
        val secretsProperties = if (secretsPropertiesFile.exists()) {
            Properties().apply {
                load(secretsPropertiesFile.inputStream())
            }
        } else {
            Properties()
        }
        spotifyClientId = secretsProperties["SPOTIFY_CLIENT_ID"] as String? ?: ""
        spotifyClientSecret = secretsProperties["SPOTIFY_CLIENT_SECRET"] as String? ?: ""
    }

    buildConfigField(
        name = "SPOTIFY_CLIENT_ID",
        value = spotifyClientId,
    )
    buildConfigField(
        name = "SPOTIFY_CLIENT_SECRET",
        value = spotifyClientSecret,
    )
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.domain)
                implementation(projects.core.logging.api)
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
            }
        }
    }
}
