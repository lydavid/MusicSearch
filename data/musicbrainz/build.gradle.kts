plugins {
    id("ly.david.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.data.core)
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.android)
//    implementation(libs.moshi.kotlin)
    implementation(libs.bundles.retrofit)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
