plugins {
    id("ly.david.kotlin")
}

dependencies {
    implementation(libs.moshi.kotlin)

    implementation(libs.room.common)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
