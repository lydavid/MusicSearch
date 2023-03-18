plugins {
    id("kotlin")
}

dependencies {
    api(project(":data:base"))
    api(project(":data:coverart"))
    api(project(":data:musicbrainz"))

    implementation(libs.moshi.kotlin)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    // Room
    implementation(libs.room.common)

    // Paging
    implementation("androidx.paging:paging-common-ktx:3.1.1")

    // Hilt
    implementation(libs.dagger)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
