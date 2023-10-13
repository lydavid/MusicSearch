package ly.david.musicsearch.domain.search.results

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

interface SearchResultsRepository {
    fun observeSearchResults(
        entity: MusicBrainzEntity,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}
