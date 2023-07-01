plugins {
    id("ly.david.kotlin")
}

dependencies {
    implementation(projects.data.base)

    implementation(libs.moshi.kotlin)

    implementation(libs.bundles.retrofit)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
