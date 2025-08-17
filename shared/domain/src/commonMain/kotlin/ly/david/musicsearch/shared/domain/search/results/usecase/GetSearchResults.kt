package ly.david.musicsearch.shared.domain.search.results.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository

private const val SEARCH_DELAY_MS = 500L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class GetSearchResults(
    private val searchResultsRepository: SearchResultsRepository,
    private val coroutineScope: CoroutineScope,
) {
    private val _searchQueries = MutableStateFlow(MusicBrainzEntityType.ARTIST to "")

    operator fun invoke(
        entity: MusicBrainzEntityType,
        query: String,
    ): Flow<PagingData<ListItemModel>> {
        _searchQueries.value = entity to query

        return _searchQueries
            .debounce(SEARCH_DELAY_MS)
            .distinctUntilChanged()
            .flatMapLatest { (currentEntity, currentQuery) ->
                when {
                    currentQuery.isBlank() -> emptyFlow()
                    else -> searchResultsRepository.observeSearchResults(
                        entity = currentEntity,
                        query = currentQuery,
                    )
                }
            }
            .cachedIn(scope = coroutineScope)
    }
}
