plugins {
    id("ly.david.android.library")
    id("kotlin-kapt")
}

android {
    namespace = "ly.david.data"

    defaultConfig {
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
        }
        release {
            isMinifyEnabled = true
            consumerProguardFiles("consumer-rules.pro")
        }
    }
}

dependencies {
    api(projects.data)

    api(libs.coil.base)
    api(libs.coil.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kaptTest(libs.hilt.android.compiler)
    implementation(libs.hilt.android.testing)
    implementation(libs.room.runtime)
}
