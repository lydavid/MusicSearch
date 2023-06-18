plugins {
    id("kotlin")
}

// TODO: is this module needed? All it does is expose its submodules
dependencies {
    api(projects.data.base)
    api(projects.data.coverart)
    api(projects.data.domain)
    api(projects.data.room)
    api(projects.data.spotify)
    api(projects.data.musicbrainz)
}
