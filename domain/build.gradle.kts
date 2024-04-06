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

                implementation(compose.runtime)
                implementation(libs.koin.annotations)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.paging.common)
            }
        }
    }
}

dependencies {
    add("kspJvm", libs.koin.ksp.compiler)
}
