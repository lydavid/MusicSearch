plugins {
    kotlin("jvm")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.compose.ui.tooling.preview)
    // https://github.com/LemonAppDev/konsist/blob/main/samples/starter-projects/konsist-starter-kmp-gradle-kotlin-junit5/konsistTest/build.gradle.kts
    testImplementation(libs.konsist)
    testImplementation(libs.junit.jupiter.engine)
    // https://github.com/gradle/gradle/issues/34512
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
