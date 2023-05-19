plugins {
    id("kotlin")
}

dependencies {
    api(project(":data:base"))
    api(project(":data:coverart"))
    api(project(":data:musicbrainz"))

    implementation(libs.androidx.paging.common)

    implementation(libs.dagger)

    implementation(libs.moshi.kotlin)

    implementation(libs.bundles.retrofit)

    implementation(libs.room.common)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
