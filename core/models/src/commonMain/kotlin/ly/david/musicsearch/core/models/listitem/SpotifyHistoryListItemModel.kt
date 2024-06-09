package ly.david.musicsearch.core.models.listitem

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.history.SpotifyHistory

data class SpotifyHistoryListItemModel(
    override val id: String,
    val trackId: String,
    val artistName: String? = null,
    val albumName: String? = null,
    val trackName: String? = null,
    val trackLengthMilliseconds: Int? = null,
    val lastListened: Instant = Clock.System.now(),
) : ListItemModel()

fun SpotifyHistory.toSpotifyHistoryListItemModel(): SpotifyHistoryListItemModel {
    return SpotifyHistoryListItemModel(
        id = "$trackId$lastListened",
        trackId = trackId,
        artistName = artistName,
        albumName = albumName,
        trackName = trackName,
        trackLengthMilliseconds = trackLengthMilliseconds,
        lastListened = lastListened,
    )
}
