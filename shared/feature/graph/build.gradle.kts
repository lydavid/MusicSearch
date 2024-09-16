plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.graph"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(libs.circuit.foundation)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.paging.common)
            implementation(libs.paging.compose)
            implementation(projects.core.preferences)
            implementation(projects.shared.domain)
            implementation(projects.ui.common)
            implementation(projects.ui.image)
        }
        }
        val androidMain by getting {
            dependencies {
            implementation(compose.preview)
        }
        }
        val androidUnitTest by getting {
            dependencies {
            // TODO: cannot use junit5 until screenshot base class stops using junit4
            implementation(libs.junit)
            //                implementation(libs.junit.jupiter.engine)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(projects.ui.test.screenshot)
        }
        }
    }
}

// tasks.withType<Test> {
//    useJUnitPlatform()
// }

dependencies {
    debugImplementation(compose.uiTooling)
}
