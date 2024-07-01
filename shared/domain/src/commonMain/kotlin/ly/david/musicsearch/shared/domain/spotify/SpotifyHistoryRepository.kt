package ly.david.musicsearch.shared.domain.spotify

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.history.SpotifyHistory
import ly.david.musicsearch.core.models.listitem.ListItemModel

interface SpotifyHistoryRepository {
    fun insert(spotifyHistory: SpotifyHistory)
    fun observeSpotifyHistory(query: String): Flow<PagingData<ListItemModel>>
    fun markAsDeleted(trackId: String, listened: Instant)
    fun undoMarkAsDeleted(trackId: String, listened: Instant)
    fun delete(trackId: String, listened: Instant)
}
