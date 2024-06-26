import java.io.FileInputStream
import java.util.Properties

plugins {
    id("ly.david.android.application")
    id("ly.david.musicsearch.compose.multiplatform")
}

if (file("google-services.json").exists() ||
    file("src/debug/google-services.json").exists() ||
    file("src/release/google-services.json").exists()
) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

android {
    namespace = "ly.david.musicsearch.android.app"

    signingConfigs {
        create("release") {

            // https://developer.android.com/studio/publish/app-signing#secure-shared-keystore
            val keystorePropertiesExists = rootProject.file("keystore.properties").exists()
            val releaseKeystoreExists = rootProject.file("release-keystore.jks").exists()

            if (keystorePropertiesExists && releaseKeystoreExists) {
                val keystorePropertiesFile = rootProject.file("keystore.properties")
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))

                storeFile = rootProject.file("release-keystore.jks")
                keyAlias = "mbjc"
                storePassword = keystoreProperties["storePassword"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    defaultConfig {
        applicationId = "io.github.lydavid.musicsearch"
        versionCode = (project.properties["VERSION_CODE"] as String).toInt()
        versionName = project.properties["VERSION_NAME"] as String

        testInstrumentationRunner = "ly.david.musicsearch.android.app.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]
        }
    }
    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/**/previous-compilation-data.bin",
            )
        }
    }
}

dependencies {
    implementation(projects.android.feature.nowplaying)
    implementation(projects.android.feature.spotify)
    implementation(projects.core.coroutines)
    implementation(projects.core.models)
    implementation(projects.core.preferences)
    implementation(projects.data.database)
    implementation(projects.shared)
    implementation(projects.ui.common)
    implementation(libs.androidx.activity.compose)
    implementation(libs.appauth)
    implementation(libs.circuit.foundation)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.timber)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.leakcanary.android)

    testImplementation(projects.testData)
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.bundles.kotlinx.coroutines)

    androidTestImplementation(projects.testData)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.bundles.kotlinx.coroutines)
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.koin.test)
    androidTestImplementation(libs.sqldelight.android.driver)
    androidTestImplementation(libs.test.parameter.injector)
}
