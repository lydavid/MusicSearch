package ly.david.musicsearch.data.spotify.api

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyArtist(
    val images: List<SpotifyImage>? = null,
)

internal fun SpotifyArtist.getLargeImageUrl(): String {
    return images?.maxByOrNull { it.width }?.url.orEmpty()
}

internal fun SpotifyArtist.getThumbnailImageUrl(): String {
    return images?.minByOrNull { it.width }?.url.orEmpty()
}
