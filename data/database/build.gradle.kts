plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("app.cash.sqldelight")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.data.core)
                implementation(libs.koin.annotations)
                implementation(libs.koin.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockk)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:2.0.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
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

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
}

// region Workaround
//  from https://github.com/google/ksp/issues/567#issuecomment-1510477456
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}
// endregion
