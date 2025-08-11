package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.SpotifyHistory
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Spotify_track
import lydavidmusicsearchdatadatabase.Spotify_track_listen

class SpotifyHistoryDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.spotify_track_listenQueries
    private val spotifyTrackTransacter = database.spotify_trackQueries

    fun insert(spotifyHistory: SpotifyHistory) {
        spotifyHistory.run {
            transacter.transaction {
                spotifyTrackTransacter.insert(
                    Spotify_track(
                        track_id = trackId,
                        artist_name = artistName,
                        album_name = albumName,
                        track_name = trackName,
                        track_length = trackLengthMilliseconds,
                    ),
                )
                transacter.insert(
                    Spotify_track_listen(
                        track_id = trackId,
                        listened = lastListened,
                        deleted = false,
                    ),
                )
            }
        }
    }

    fun getAllSpotifyHistory(
        query: String,
    ): PagingSource<Int, SpotifyHistory> = QueryPagingSource(
        countQuery = transacter.getAllSpotifyHistoryCount(
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllSpotifyHistory(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToSpotifyHistory,
            )
        },
    )

    fun markAsDeleted(
        trackId: String,
        listened: Instant,
        deleted: Boolean,
    ) {
        transacter.markAsDeleted(
            deleted = deleted,
            trackId = trackId,
            listened = listened,
        )
    }

    fun delete(
        trackId: String,
        listened: Instant,
    ) {
        transacter.delete(
            trackId = trackId,
            listened = listened,
        )
    }
}

private fun mapToSpotifyHistory(
    trackId: String,
    artistName: String?,
    albumName: String?,
    trackName: String?,
    trackLength: Int?,
    lastListened: Instant?,
) = SpotifyHistory(
    trackId = trackId,
    artistName = artistName,
    albumName = albumName,
    trackName = trackName,
    trackLengthMilliseconds = trackLength,
    lastListened = lastListened ?: Clock.System.now(),
)
