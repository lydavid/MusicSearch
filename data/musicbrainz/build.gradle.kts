plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.data.core)
                implementation(libs.koin.annotations)
                implementation(libs.koin.core)
                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.koin.test)
                implementation(libs.mockk)
            }
        }
    }
}

dependencies {
    add("kspJvm", libs.koin.ksp.compiler)
}
