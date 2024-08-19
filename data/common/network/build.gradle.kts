plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ly.david.musicsearch.data.common.network"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.logging.api)
                implementation(libs.koin.core)
//                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.bundles.ktor.jvm)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.bundles.ktor.jvm)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.noop)
}
