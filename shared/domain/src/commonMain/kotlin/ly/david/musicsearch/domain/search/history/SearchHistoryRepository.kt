package ly.david.musicsearch.domain.search.history

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

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
