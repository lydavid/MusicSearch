package ly.david.data.network.api.coverart

internal fun String.extractPathFromUrl(): String {
    val splitUrl = this.split("/")
    return if (splitUrl.size < 2) {
        ""
    } else {
        "${splitUrl[splitUrl.lastIndex - 1]}/${splitUrl.last().replace(".jpg", "")}"
    }
}
