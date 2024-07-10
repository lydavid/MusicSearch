plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.coroutines)
                implementation(projects.core.models)
                implementation(projects.data.musicbrainz)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.paging.common)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.sqldelight.paging)
                implementation(libs.sqldelight.primitive)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockk)
            }
        }
        val jvmCommon by creating {
            dependsOn(commonMain)
        }
        val androidMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.sqldelight.android.driver)
            }
        }
        val jvmMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.sqldelight.native.driver)
            }
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("ly.david.musicsearch.data.database")
        }
    }
}

android {
    namespace = "ly.david.musicsearch.data.database"
}
