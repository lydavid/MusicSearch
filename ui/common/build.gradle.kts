plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.ui.common"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.data.coverart)
                implementation(projects.data.musicbrainz)
                implementation(projects.domain)
                implementation(projects.strings)
                implementation(projects.ui.core)
                implementation(projects.ui.image)

                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.preview)

                implementation(libs.lyricist.library)

//                androidTestImplementation(libs.compose.ui.test)

//                testImplementation(projects.ui.test.image)
//                testImplementation(projects.ui.test.screenshot)
//                testImplementation(libs.bundles.kotlinx.coroutines)
//                testImplementation(libs.coil.compose)
//                testImplementation(libs.coil.test)
//                testImplementation(libs.test.parameter.injector)
//                testImplementation(libs.koin.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.constraintlayout.compose)
            }
        }
        val jvmMain by getting
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
