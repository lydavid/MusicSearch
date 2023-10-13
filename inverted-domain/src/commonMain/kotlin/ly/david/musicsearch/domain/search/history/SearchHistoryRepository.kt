package ly.david.musicsearch.domain.search.history

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

interface SearchHistoryRepository {
    fun observeSearchHistory(
        entity: MusicBrainzEntity,
    ): Flow<PagingData<ListItemModel>>

    fun recordSearchHistory(
        entity: MusicBrainzEntity,
        query: String,
    )

    fun deleteAll(
        entity: MusicBrainzEntity,
    )

    fun delete(
        entity: MusicBrainzEntity,
        query: String,
    )
}
