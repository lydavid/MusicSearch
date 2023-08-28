plugins {
    id("ly.david.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.data.core)

    implementation(libs.dagger)

    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.android)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
