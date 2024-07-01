package ly.david.musicsearch.shared.domain.search.results

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface SearchResultsRepository {
    fun observeSearchResults(
        entity: MusicBrainzEntity,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}
