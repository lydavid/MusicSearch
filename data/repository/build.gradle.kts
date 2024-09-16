import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.powerAssert)
}

android {
    namespace = "ly.david.musicsearch.data.repository"
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
powerAssert {
    functions = listOf(
        "kotlin.test.assertEquals",
        "kotlin.test.assertNull",
        "kotlin.test.assertNotNull",
    )
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.paging.common)
            implementation(projects.core.logging.api)
            implementation(projects.data.common.network)
            implementation(projects.data.database)
            implementation(projects.data.musicbrainz)
            implementation(projects.shared.domain)
        }
        }
        val commonTest by getting {
            dependencies {
            implementation(libs.androidx.paging.testing)
            implementation(libs.junit)
            implementation(libs.koin.test)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.core.coroutines)
            implementation(projects.testData)
        }
        }
        val androidUnitTest by getting {
            dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
        }
        val jvmTest by getting {
            dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
        }
    }
}
