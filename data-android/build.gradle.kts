plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "ly.david.data"

    defaultConfig {
        // Need this or else we won't be able to compile androidTest for this module
        manifestPlaceholders += mapOf("appAuthRedirectScheme" to "")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isTestCoverageEnabled = true
        }
        getByName("release") {
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
    api(project(":data"))
    testImplementation(project(":test-data"))

    implementation(libs.moshi.kotlin)

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("net.openid:appauth:0.11.1")

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    kaptTest(libs.hilt.android.compiler)
    testImplementation(libs.hilt.android.testing)

    testImplementation(libs.junit)
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("org.robolectric:robolectric:4.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
}
