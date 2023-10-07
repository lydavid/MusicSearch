plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.data)
                implementation(projects.invertedDomain)
                implementation(libs.koin.core)
            }
        }
    }
}
