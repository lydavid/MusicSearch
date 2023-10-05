package ly.david.ui.spotify

data class SpotifyMetadata(
    val trackId: String? = null,
    val artistName: String? = null,
    val albumName: String? = null,
    val trackName: String? = null,
    val trackLengthInSec: Int? = null,
    val timeSentInMs: Long? = null,
)
