plugins {
    id("ly.david.kotlin")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(projects.data.core)
    implementation(projects.data.common.network)
    implementation(projects.data.coverart)
    implementation(projects.data.database)
    implementation(projects.data.musicbrainz)
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.paging.common)

    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
