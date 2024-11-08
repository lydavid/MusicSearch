package ly.david.musicsearch.shared.domain.search.results.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository

class GetSearchResults(
    private val searchResultsRepository: SearchResultsRepository,
    private val coroutineScope: CoroutineScope,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
        query: String,
    ): Flow<PagingData<ListItemModel>> {
        return when {
            query.isBlank() -> emptyFlow()
            else -> searchResultsRepository.observeSearchResults(
                entity = entity,
                query = query,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
