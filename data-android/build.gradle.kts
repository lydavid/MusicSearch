import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "ly.david.data"

    defaultConfig {
        // Need this or else we won't be able to compile androidTest for this module
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        all {
            // TODO: will this work if CI reads uses ORG_GRADLE_PROJECT_SPOTIFY_CLIENT_ID?
            val secretsPropertiesFile = rootProject.file("secrets.properties")
            val secretsProperties: Properties
            if (secretsPropertiesFile.exists()) {
                secretsProperties = Properties().apply {
                    load(secretsPropertiesFile.inputStream())
                }
            } else {
                secretsProperties = Properties()
            }
            buildConfigField(
                type = "String",
                name = "SPOTIFY_CLIENT_ID",
                value = "\"${secretsProperties["SPOTIFY_CLIENT_ID"] as String? ?: ""}\""
            )
            buildConfigField(
                type = "String",
                name = "SPOTIFY_CLIENT_SECRET",
                value = "\"${secretsProperties["SPOTIFY_CLIENT_SECRET"] as String? ?: ""}\""
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
    kapt(libs.hilt.android.compiler)
    kaptTest(libs.hilt.android.compiler)
    testImplementation(libs.hilt.android.testing)

    implementation(libs.moshi.kotlin)

    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.bundles.retrofit)

    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)

    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.junit)
    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.robolectric)
}
