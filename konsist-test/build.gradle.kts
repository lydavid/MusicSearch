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
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(compose.preview)
    // https://github.com/LemonAppDev/konsist/blob/main/samples/starter-projects/konsist-starter-kmp-gradle-kotlin-junit5/konsistTest/build.gradle.kts
    testImplementation(libs.konsist)
    testImplementation(libs.junit.jupiter.engine)
}
