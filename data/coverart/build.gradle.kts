plugins {
    id("ly.david.kotlin")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(projects.data.core)
    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.android)

    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
