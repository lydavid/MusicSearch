plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("kotlin-parcelize")
//    alias(libs.plugins.ksp)
//    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.feature.search"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.domain)
                implementation(projects.strings)

                implementation(projects.ui.core)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.preview)
                implementation(compose.ui)
                implementation(libs.koin.core)
                implementation(libs.circuit.foundation)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.ui.common)
                implementation(libs.koin.androidx.compose)
                implementation(libs.androidx.paging.compose)
                implementation(libs.androidx.paging.runtime)
            }
        }
        val jvmMain by getting
    }
}

dependencies {

//    implementation(libs.koin.annotations)

    debugImplementation(libs.compose.ui.tooling)
//
//    ksp(libs.koin.ksp.compiler)

//    testImplementation(projects.ui.test.screenshot)
//    testImplementation(libs.test.parameter.injector)
}
