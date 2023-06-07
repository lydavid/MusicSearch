plugins {
    id("kotlin")
}

dependencies {
    implementation(projects.data.base)

    implementation(libs.moshi.kotlin)

    implementation(libs.bundles.retrofit)
}
