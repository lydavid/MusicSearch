package ly.david.musicsearch.android.feature.spotify.internal

/**
 * Example format
 *
 * ```
 * SpotifyMetadata(
 *     trackId="spotify:track:5PFxNrRme9z6jiprt8AfqX",
 *     artistName="[Alexandros]",
 *     albumName="Where's My History?",
 *     trackName="ワタリドリ",
 *     trackLengthInSec=253600,
 *     timeSentInMs=1712974970504
 * )
 * ```
 */
internal data class SpotifyMetadata(
    val trackId: String? = null,
    val artistName: String? = null,
    val albumName: String? = null,
    val trackName: String? = null,
    val trackLengthInSec: Int? = null,
    val timeSentInMs: Long? = null,
)
