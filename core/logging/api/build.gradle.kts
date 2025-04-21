plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.coroutines)
            }
        }
    }
}
