package ly.david.data.spotify

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyArtist(
    val images: List<SpotifyImage>,
)

fun SpotifyArtist.getLargeImageUrl(): String {
    return images.maxByOrNull { it.width }?.url.orEmpty()
}

fun SpotifyArtist.getThumbnailImageUrl(): String {
    return images.minByOrNull { it.width }?.url.orEmpty()
}
