import java.util.Properties

plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.build.config)
}

android {
    namespace = "ly.david.musicsearch.data.musicbrainz"
}

buildConfig {
    var musicBrainzClientId = project.properties["MUSICBRAINZ_CLIENT_ID"] as String?
    var musicBrainzClientSecret = project.properties["MUSICBRAINZ_CLIENT_SECRET"] as String?

    if (musicBrainzClientId.isNullOrEmpty() || musicBrainzClientSecret.isNullOrEmpty()) {
        val secretsPropertiesFile = rootProject.file("secrets.properties")
        val secretsProperties = if (secretsPropertiesFile.exists()) {
            Properties().apply {
                load(secretsPropertiesFile.inputStream())
            }
        } else {
            Properties()
        }

        // Cannot be null or empty so that we can run tests on CI without passing these in
        musicBrainzClientId = secretsProperties["MUSICBRAINZ_CLIENT_ID"] as String? ?: "-"
        musicBrainzClientSecret = secretsProperties["MUSICBRAINZ_CLIENT_SECRET"] as String? ?: "-"
    }

    buildConfigField(
        type = "String",
        name = "MUSICBRAINZ_CLIENT_ID",
        value = "\"$musicBrainzClientId\"",
    )
    buildConfigField(
        type = "String",
        name = "MUSICBRAINZ_CLIENT_SECRET",
        value = "\"$musicBrainzClientSecret\"",
    )
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.logging.api)
                implementation(projects.core.models)
                implementation(libs.koin.core)
                implementation(libs.androidx.datastore.preferences.core)
                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.koin.test)
                implementation(libs.mockk)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.appauth)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.github.scribejava:scribejava-apis:8.3.3")
            }
        }
    }
}
