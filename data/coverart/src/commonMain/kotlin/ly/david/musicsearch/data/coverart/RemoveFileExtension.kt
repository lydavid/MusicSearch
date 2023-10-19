package ly.david.musicsearch.data.coverart

/**
 * File formats supported by Cover Art Archive:
 * https://musicbrainz.org/doc/How_to_Add_Cover_Art
 */
private val fileFormats = listOf(
    "gif", "jpg", "jpeg", "png", "htm", "html", "jpe", "jfif", "pdf",
)

internal fun String.removeFileExtension(): String {
    fileFormats.forEach { format ->
        val ext = ".$format"
        if (this.contains(ext)) {
            return this.replace(ext, "")
        }
    }
    return this
}
