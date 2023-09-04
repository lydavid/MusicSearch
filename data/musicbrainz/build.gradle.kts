plugins {
    id("ly.david.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.data.core)
    implementation(projects.data.common.network)
    implementation(libs.dagger)
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.android)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
