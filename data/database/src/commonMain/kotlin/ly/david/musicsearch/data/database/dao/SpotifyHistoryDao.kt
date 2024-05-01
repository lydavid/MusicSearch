package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.core.models.history.SpotifyHistory
import ly.david.musicsearch.data.database.Database

class SpotifyHistoryDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.spotify_historyQueries

    fun upsert(spotifyHistory: SpotifyHistory) {
        spotifyHistory.run {
            transacter.upsert(
                trackId = trackId,
                artistName = artistName,
                albumName = albumName,
                trackName = trackName,
                trackLength = trackLengthMilliseconds,
                numberOfListens = numberOfListens,
                lastListened = lastListened,
            )
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
}

private fun mapToSpotifyHistory(
    trackId: String,
    artistName: String?,
    albumName: String?,
    trackName: String?,
    trackLength: Int?,
    numberOfListens: Int,
    lastListened: Instant,
) = SpotifyHistory(
    trackId = trackId,
    artistName = artistName,
    albumName = albumName,
    trackName = trackName,
    trackLengthMilliseconds = trackLength,
    numberOfListens = numberOfListens,
    lastListened = lastListened,
)
