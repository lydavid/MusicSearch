plugins {
    id("ly.david.android.library")
    kotlin("android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "ly.david.data"

    buildTypes {
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

    implementation(libs.androidx.datastore.preferences.android)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.appauth)

    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.jvm)

    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.timber)

    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.bundles.kotlinx.coroutines)
    testImplementation(libs.robolectric)
}
