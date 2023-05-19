plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "ly.david.data"

    defaultConfig {
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
}

dependencies {
    api(project(":data"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kaptTest(libs.hilt.android.compiler)
    implementation(libs.hilt.android.testing)

    implementation(libs.room.runtime)
}
