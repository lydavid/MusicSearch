package ly.david.musicsearch.shared.domain.search.history

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface SearchHistoryRepository {
    fun observeSearchHistory(
        entity: MusicBrainzEntityType,
    ): Flow<PagingData<ListItemModel>>

    fun recordSearchHistory(
        entity: MusicBrainzEntityType,
        query: String,
    )

    fun deleteAll(
        entity: MusicBrainzEntityType,
    )

    fun delete(
        entity: MusicBrainzEntityType,
        query: String,
    )
}
