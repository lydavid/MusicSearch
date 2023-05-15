import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
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
        versionCode = 71
        versionName = "0.7.2"

        testInstrumentationRunner = "ly.david.mbjc.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

//        kapt {
//            arguments {
//                arg("room.schemaLocation", "$projectDir/schemas")
//            }
//        }
    }

    buildTypes {
        getByName("debug") {
//            minifyEnabled true
//            shrinkResources true
//            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"

            isTestCoverageEnabled = true
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    // TODO: Not working on Windows 10: https://issuetracker.google.com/issues/249111286
//    testOptions {
//        managedDevices {
//            devices {
//                pixel2api30 (com.android.build.api.dsl.ManagedVirtualDevice) {
//                    // Use device profiles you typically see in Android Studio.
//                    device = "Pixel 2"
//                    // Use only API levels 27 and higher.
//                    apiLevel = 30
//                    // To include Google services, use "google".
//                    systemImageSource = "aosp"
//                }
//            }
//        }
//    }
}

dependencies {
    implementation(project(":data-android"))
    testImplementation(project(":test-data"))
    androidTestImplementation(project(":test-data"))

    implementation(libs.accompanist.swiperefresh)
    implementation(libs.accompanist.pager)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.appauth)

    implementation(libs.coil.base)
    implementation(libs.coil.compose)
    androidTestImplementation(libs.coil.test)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    kaptAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)

    testImplementation(libs.junit)

    testImplementation(libs.bundles.kotlinx.coroutines)
    androidTestImplementation(libs.bundles.kotlinx.coroutines)

    implementation(libs.bundles.retrofit)

    androidTestImplementation(libs.room.testing)

    androidTestImplementation(libs.okhttp.mockwebserver)
    androidTestImplementation(libs.okhttp.tls)

    implementation(libs.timber)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.arch.core.testing)
}

if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}
