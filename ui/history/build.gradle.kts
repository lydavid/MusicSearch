plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
//    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ly.david.ui.history"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.core.parcelize)
                implementation(projects.core.preferences)
                implementation(projects.domain)
                implementation(projects.strings)
                implementation(projects.ui.common)
                implementation(projects.ui.core)
                implementation(projects.ui.image)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation("com.slack.circuit:circuit-foundation:0.19.1")
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
                implementation(libs.koin.core)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.image)
                implementation(projects.ui.test.screenshot)
                implementation(libs.test.parameter.injector)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.coil.compose)
            }
        }
    }
}
dependencies {
    debugImplementation(compose.uiTooling)

//    implementation(projects.ui.commonLegacy)






//    implementation(libs.koin.androidx.compose)

//    implementation(libs.koin.annotations)
//    ksp(libs.koin.ksp.compiler)


}
