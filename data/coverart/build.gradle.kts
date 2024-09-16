plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            implementation(libs.koin.core)
            //                implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(projects.core.logging.api)
            implementation(projects.shared.domain)
        }
        }
        val commonTest by getting {
            dependencies {
            implementation(libs.kotlin.test)
        }
        }
    }
}
