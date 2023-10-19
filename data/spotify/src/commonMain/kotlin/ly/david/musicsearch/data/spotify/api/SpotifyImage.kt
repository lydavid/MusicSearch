package ly.david.musicsearch.data.spotify.api

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyImage(
    val url: String,
    val height: Int,
    val width: Int,
)
