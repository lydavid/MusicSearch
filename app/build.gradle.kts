import java.io.FileInputStream
import java.util.Properties

plugins {
    id("ly.david.android.application")
    id("ly.david.android.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

if (file("google-services.json").exists() ||
    file("src/debug/google-services.json").exists() ||
    file("src/release/google-services.json").exists()
) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

android {
    namespace = "ly.david.mbjc"

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
        versionCode = (project.properties["VERSION_CODE"] as String?)?.toInt()
        versionName = project.properties["VERSION_NAME"] as String?

        testInstrumentationRunner = "ly.david.mbjc.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isTestCoverageEnabled = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]
        }
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.dataAndroid)
    implementation(projects.data.database)
    implementation(projects.data.repository)
    implementation(projects.domain)
    implementation(projects.invertedDomain)
    implementation(projects.feature.search)
    implementation(projects.strings)
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(projects.ui.collections)
    implementation(projects.ui.history)
    implementation(projects.ui.image)
    implementation(projects.ui.nowplaying)
    implementation(projects.ui.settings)
    implementation(projects.ui.spotifyBroadcastReceiver)
    implementation(projects.ui.stats)
    implementation(libs.accompanist.pager)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.appauth)
    implementation(libs.coil.base)
    implementation(libs.coil.compose)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.annotations)
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
    androidTestImplementation(libs.coil.test)
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.koin.test)
    androidTestImplementation(libs.sqldelight.android.driver)
    androidTestImplementation(libs.test.parameter.injector)

    ksp(libs.koin.ksp.compiler)
    kspAndroidTest(libs.koin.ksp.compiler)
}
