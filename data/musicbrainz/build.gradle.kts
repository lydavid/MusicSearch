plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":data:base"))

    implementation(libs.moshi.kotlin)

    implementation(libs.bundles.retrofit)
}
