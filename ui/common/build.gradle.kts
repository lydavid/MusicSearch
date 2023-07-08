plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("kotlin-kapt")
    id("app.cash.paparazzi")
}

android {
    namespace = "ly.david.ui.common"

    buildTypes {
        // Can't seem to run paparazzi screenshot tests with code coverage
//        debug {
//            enableUnitTestCoverage = true
//        }

        release {
            isMinifyEnabled = true
            consumerProguardFiles("consumer-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.dataAndroid)
    implementation(projects.ui.core)
    implementation(projects.ui.image)
    testImplementation(projects.testData)
    testImplementation(projects.ui.test.image)
    testImplementation(projects.ui.test.screenshot)

    implementation(libs.accompanist.swiperefresh)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.coil.base)
    implementation(libs.coil.compose)
    testImplementation(libs.coil.test)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    kaptAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)

    implementation(libs.timber)

    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.coil.compose)
    testImplementation(libs.test.parameter.injector)
}
