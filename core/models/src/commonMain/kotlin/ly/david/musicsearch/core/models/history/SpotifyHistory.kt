package ly.david.musicsearch.core.models.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Example format of the data that comes from Spotify's [broadcast notification](https://developer.spotify.com/documentation/android/tutorials/android-media-notifications).
 *
 * ```
 * SpotifyHistory(
 *     trackId="spotify:track:5PFxNrRme9z6jiprt8AfqX",
 *     artistName="[Alexandros]",
 *     albumName="Where's My History?",
 *     trackName="ワタリドリ",
 *     trackLengthMilliseconds=253600,
 *     timeSentInMs=1712974970504
 * )
 * ```
 */
data class SpotifyHistory(
    val trackId: String,
    val artistName: String? = null,
    val albumName: String? = null,
    val trackName: String? = null,
    val trackLengthMilliseconds: Int? = null,
//    val timeSentInMs: Long? = null,

    val numberOfListens: Int = 1,
    val lastListened: Instant = Clock.System.now(),
)
