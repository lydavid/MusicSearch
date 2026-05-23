import com.android.build.api.dsl.androidLibrary

plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
}



kotlin {
    androidLibrary {
        namespace = "ly.david.musicsearch.konsist"
    }
    sourceSets {
        val androidMain by getting {
            dependencies {

            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.compose.ui.tooling.preview)
    // https://github.com/LemonAppDev/konsist/blob/main/samples/starter-projects/konsist-starter-kmp-gradle-kotlin-junit5/konsistTest/build.gradle.kts
    testImplementation(libs.konsist)
    testImplementation(libs.junit.jupiter.engine)
}
