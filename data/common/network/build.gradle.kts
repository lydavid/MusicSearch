plugins {
    id("ly.david.kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.android)
}
