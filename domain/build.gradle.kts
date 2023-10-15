plugins {
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.data.core)
                implementation(projects.data.common.network)
                implementation(projects.data.coverart)
                implementation(projects.data.database)
                implementation(projects.data.musicbrainz)
                implementation(compose.runtime)
                implementation(libs.koin.annotations)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.paging.common)
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

dependencies {
    add("kspJvm", libs.koin.ksp.compiler)
}
