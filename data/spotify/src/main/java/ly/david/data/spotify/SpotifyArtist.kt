package ly.david.data.spotify

data class SpotifyArtist(
    val images: List<SpotifyImage>,
)

fun SpotifyArtist.getLargeImageUrl(): String {
    return images.maxBy { it.width }.url
}

fun SpotifyArtist.getThumbnailImageUrl(): String {
    return images.minBy { it.width }.url
}
