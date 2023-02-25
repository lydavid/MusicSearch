plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":data:base"))

    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
}
