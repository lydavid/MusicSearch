plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.bundles.ktor.android)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockk)
            }
        }
    }
}
