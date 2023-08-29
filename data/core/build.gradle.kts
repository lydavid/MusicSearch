plugins {
    id("ly.david.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
//    implementation(libs.moshi.kotlin)

    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.android)
    implementation(libs.room.common)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
