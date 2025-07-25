import java.io.FileInputStream
import java.util.Properties

plugins {
    id("ly.david.android.application")
    id("ly.david.musicsearch.compose.multiplatform")
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.aboutlibraries)
}

// For F-Droid, remove google-services.json and we will not apply these plugins.
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
            vcsInfo.include = false
        }
    }

    flavorDimensions += "appStore"
    productFlavors {
        create("fDroid") {
            dimension = "appStore"
        }
        create("googlePlay") {
            dimension = "appStore"
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

    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }
}

dependencies {
    implementation(projects.shared.feature.nowplaying)
    implementation(projects.shared.feature.spotify)
    implementation(projects.core.logging.api)
    implementation(projects.shared.domain)
    implementation(projects.shared)
    implementation(projects.ui.common)
    implementation(libs.androidx.activity.compose)
    implementation(libs.appauth)
    implementation(libs.circuit.foundation)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.timber)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(projects.android.baselineprofile)

    googlePlayImplementation(platform(libs.firebase.bom))
    googlePlayImplementation(libs.firebase.analytics)
    googlePlayImplementation(libs.firebase.crashlytics)

    debugImplementation(libs.leakcanary.android)
}

fun DependencyHandler.googlePlayImplementation(dependencyNotation: Any) =
    add("googlePlayImplementation", dependencyNotation)

aboutLibraries {
    excludeFields = arrayOf("generated")
    outputPath = "../../shared/feature/licenses/src/commonMain/composeResources/files/"

    prettyPrint = true
    android {
        registerAndroidTasks = false
    }
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
        duplicationRule = com.mikepenz.aboutlibraries.plugin.DuplicateRule.GROUP
    }
}
