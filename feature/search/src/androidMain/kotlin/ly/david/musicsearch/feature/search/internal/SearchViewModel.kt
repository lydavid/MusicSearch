package ly.david.musicsearch.feature.search.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.domain.search.results.usecase.GetSearchResults

private const val SEARCH_DELAY_MS = 500L

internal class SearchViewModel(
    private val getSearchResults: GetSearchResults,
    private val getSearchHistory: GetSearchHistory,
    private val recordSearchHistory: RecordSearchHistory,
    private val deleteSearchHistory: DeleteSearchHistory,
) : ViewModel() {

    private data class ViewModelState(
        val query: String,
        val entity: MusicBrainzEntity,
    )

    val searchQuery = MutableStateFlow("")
    val searchEntity = MutableStateFlow(MusicBrainzEntity.ARTIST)
    private val viewModelState = combine(
        searchQuery,
        searchEntity,
    ) { query, entity ->
        ViewModelState(
            query,
            entity,
        )
    }

    fun search(
        query: String? = null,
        entity: MusicBrainzEntity? = null,
    ) {
        if (query != null) {
            searchQuery.value = query
        }
        if (entity != null) {
            searchEntity.value = entity
        }
    }

    fun clearQuery() {
        searchQuery.value = ""
    }

    fun recordSearchHistory() {
        val query = searchQuery.value
        if (query.isBlank()) return
        val entity = searchEntity.value
        recordSearchHistory(
            entity,
            query,
        )
    }

    fun deleteSearchHistoryItem(item: SearchHistoryListItemModel) {
        deleteSearchHistory(
            entity = item.entity,
            query = item.query,
        )
    }

    fun deleteAllSearchHistoryForEntity() {
        deleteSearchHistory(entity = searchEntity.value)
    }

    @OptIn(
        ExperimentalCoroutinesApi::class,
        FlowPreview::class,
    )
    val searchResults: Flow<PagingData<ListItemModel>> =
        viewModelState.filterNot { it.query.isBlank() }
            .debounce(SEARCH_DELAY_MS)
            .flatMapLatest { viewModelState ->
                getSearchResults(
                    entity = viewModelState.entity,
                    query = viewModelState.query,
                )
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchHistory: Flow<PagingData<ListItemModel>> =
        viewModelState.filter { it.query.isBlank() }
            .flatMapLatest { viewModelState ->
                getSearchHistory(viewModelState.entity)
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
