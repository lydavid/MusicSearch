plugins {
    id("ly.david.kotlin")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(projects.data.core)
    implementation(projects.data.common.network)
    implementation(projects.data.coverart)
    implementation(projects.data.musicbrainz)
    implementation(projects.data.room)
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.androidx.paging.common)
    implementation(libs.dagger)

    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
