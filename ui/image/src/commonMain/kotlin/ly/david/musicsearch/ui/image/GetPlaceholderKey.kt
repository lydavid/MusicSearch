package ly.david.musicsearch.ui.image

fun getPlaceholderKey(mbid: String, index: Int = 0): String {
    return "${mbid}_$index"
}
