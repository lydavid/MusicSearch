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
        versionCode = 61
        versionName = "0.5.10"

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
        kotlinCompilerExtensionVersion = "1.4.6"
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

    implementation(platform("com.google.firebase:firebase-bom:31.5.0"))

    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    implementation("androidx.core:core-ktx:1.10.0")

    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)

    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    implementation(libs.accompanist.swiperefresh)
    implementation(libs.accompanist.pager)

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")

    // https://developer.android.com/jetpack/androidx/releases/paging#1.0.0-alpha15
    // LazyPagingItems now sets the initial loadState to have a LoadState.Loading refresh.
    // https://issuetracker.google.com/issues/224855902
    implementation("androidx.paging:paging-compose:1.0.0-alpha14")

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    // Room
    androidTestImplementation(libs.room.testing)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    kaptAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)

    implementation(libs.coil.base)
    implementation(libs.coil.compose)
    androidTestImplementation(libs.coil.test)

    // Tried this for UScript.getName(UScript.getCodeFromName(it))
//    implementation "com.ibm.icu:icu4j:72.1"

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("net.openid:appauth:0.11.1")

    // Test - General
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
    androidTestImplementation("com.squareup.okhttp3:okhttp-tls:4.11.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
}

if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}
