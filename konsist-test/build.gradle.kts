plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.konsist"
}

kotlin {
    sourceSets {
        val androidMain by getting
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.compose.ui.preview)
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    testImplementation(projects.ui.core)
    // https://github.com/LemonAppDev/konsist/blob/main/samples/starter-projects/konsist-starter-kmp-gradle-kotlin-junit5/konsistTest/build.gradle.kts
    testImplementation(libs.konsist)
    testImplementation(libs.junit.jupiter.engine)
}
