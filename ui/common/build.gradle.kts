plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ly.david.ui.common"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.models)
                implementation(projects.data.coverart)
                implementation(projects.data.musicbrainz)
                implementation(projects.domain)
                implementation(projects.strings)
                implementation(projects.ui.core)
                implementation(projects.ui.image)

                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation(libs.koin.annotations)
                implementation(libs.koin.core)
                implementation(libs.lyricist.library)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
            }
        }
        val jvmMain by getting
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.image)
                implementation(projects.ui.test.screenshot)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.coil.compose)
                implementation(libs.coil.test)
                implementation(libs.test.parameter.injector)
                implementation(libs.koin.test)
            }
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspJvm", libs.koin.ksp.compiler)
}
