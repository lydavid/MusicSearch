package ly.david.musicsearch.domain.spotify

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.history.SpotifyHistory
import ly.david.musicsearch.core.models.listitem.ListItemModel

interface SpotifyHistoryRepository {
    fun insert(spotifyHistory: SpotifyHistory)
    fun observeSpotifyHistory(query: String): Flow<PagingData<ListItemModel>>
    fun delete(raw: String)
}
