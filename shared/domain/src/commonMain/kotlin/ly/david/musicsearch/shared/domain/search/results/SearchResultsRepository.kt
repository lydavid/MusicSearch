package ly.david.musicsearch.shared.domain.search.results

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface SearchResultsRepository {
    fun observeSearchResults(
        entity: MusicBrainzEntity,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}
