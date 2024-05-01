package ly.david.musicsearch.data.repository

import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.insertSeparators
import app.cash.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.models.common.getDateFormatted
import ly.david.musicsearch.core.models.history.SpotifyHistory
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.core.models.listitem.toSpotifyHistoryListItemModel
import ly.david.musicsearch.data.database.dao.SpotifyHistoryDao
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.domain.spotify.SpotifyHistoryRepository

class SpotifyHistoryRepositoryImpl(
    private val spotifyHistoryDao: SpotifyHistoryDao,
) : SpotifyHistoryRepository {
    override fun upsert(spotifyHistory: SpotifyHistory) {
        // Debounce if the track is the same, and the time last listened is within its length.
        // We need this because exiting and returning to this screen will continuously upsert the current playing song.
        // Also, need to not upsert when the user is not playing anything, because the last played song will be kept.

        spotifyHistoryDao.upsert(spotifyHistory)
    }

    override fun observeSpotifyHistory(query: String): Flow<PagingData<ListItemModel>> =
        Pager(
            config = CommonPagingConfig.pagingConfig,
            pagingSourceFactory = {
                spotifyHistoryDao.getAllSpotifyHistory(
                    query = query,
                )
            },
        ).flow.map { pagingData ->
            pagingData
                .map(SpotifyHistory::toSpotifyHistoryListItemModel)
                .insertSeparators(generator = ::generator)
        }

    private fun generator(
        before: SpotifyHistoryListItemModel?,
        after: SpotifyHistoryListItemModel?,
    ): ListSeparator? {
        val beforeDate = before?.lastListened?.getDateFormatted()
        val afterDate = after?.lastListened?.getDateFormatted()
        return if (beforeDate != afterDate && afterDate != null) {
            ListSeparator(
                id = afterDate,
                text = afterDate,
            )
        } else {
            null
        }
    }

    override fun delete(raw: String) {
        TODO("Not yet implemented")
    }
}
