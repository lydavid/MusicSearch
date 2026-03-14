package ly.david.musicsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.SpotifyHistoryDao
import ly.david.musicsearch.shared.domain.common.getFullDateFormatted
import ly.david.musicsearch.shared.domain.history.SpotifyHistory
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.shared.domain.listitem.toSpotifyHistoryListItemModel
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.spotify.SpotifyHistoryRepository
import kotlin.time.Instant

class SpotifyHistoryRepositoryImpl(
    private val spotifyHistoryDao: SpotifyHistoryDao,
    private val coroutineScope: CoroutineScope,
) : SpotifyHistoryRepository {
    override fun insert(spotifyHistory: SpotifyHistory) {
        spotifyHistoryDao.insert(spotifyHistory)
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
            .distinctUntilChanged()
            .cachedIn(scope = coroutineScope)

    private fun generator(
        before: SpotifyHistoryListItemModel?,
        after: SpotifyHistoryListItemModel?,
    ): ListSeparator? {
        val beforeDate = before?.lastListened?.getFullDateFormatted()
        val afterDate = after?.lastListened?.getFullDateFormatted()
        return if (beforeDate != afterDate && afterDate != null) {
            ListSeparator(
                id = afterDate,
                text = afterDate,
            )
        } else {
            null
        }
    }

    override fun markAsDeleted(
        trackId: String,
        listened: Instant,
    ) {
        spotifyHistoryDao.markAsDeleted(
            trackId = trackId,
            listened = listened,
            deleted = true,
        )
    }

    override fun undoMarkAsDeleted(
        trackId: String,
        listened: Instant,
    ) {
        spotifyHistoryDao.markAsDeleted(
            trackId = trackId,
            listened = listened,
            deleted = false,
        )
    }

    override fun delete(
        trackId: String,
        listened: Instant,
    ) {
        spotifyHistoryDao.delete(
            trackId = trackId,
            listened = listened,
        )
    }
}
