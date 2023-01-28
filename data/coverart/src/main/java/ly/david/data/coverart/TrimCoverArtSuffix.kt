package ly.david.data.coverart

fun String.trimCoverArtSuffix(): String {
    return this.replace(SMALL_SUFFIX, "").replace(LARGE_SUFFIX, "")
}
