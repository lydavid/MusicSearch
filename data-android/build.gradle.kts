import java.util.Properties

plugins {
    id("ly.david.android.library")
    kotlin("android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "ly.david.data"

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        all {
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
                type = "String",
                name = "SPOTIFY_CLIENT_ID",
                value = "\"$spotifyClientId\""
            )
            buildConfigField(
                type = "String",
                name = "SPOTIFY_CLIENT_SECRET",
                value = "\"$spotifyClientSecret\""
            )

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
                value = "\"$musicBrainzClientId\""
            )
            buildConfigField(
                type = "String",
                name = "MUSICBRAINZ_CLIENT_SECRET",
                value = "\"$musicBrainzClientSecret\""
            )
        }
        debug {
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = true
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    configurations {
        all {
            exclude(group = "androidx.lifecycle", module = "lifecycle-runtime-ktx")
            exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
        }
    }
}

dependencies {
    api(projects.data)
    testImplementation(projects.testData)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.appauth)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    kspTest(libs.hilt.android.compiler)
    testImplementation(libs.hilt.android.testing)
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.datetime)
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.android)

    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.timber)

    ksp(libs.koin.ksp.compiler)
    ksp(libs.room.compiler)

    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.robolectric)
}
