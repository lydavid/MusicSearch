package ly.david.mbjc.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.domain.listitem.EndOfList
import ly.david.data.domain.listitem.Header
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.SearchHistoryListItemModel
import ly.david.data.domain.listitem.toSearchHistoryListItemModel
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.domain.paging.SearchMusicBrainzPagingSource
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.history.search.SearchHistoryDao
import ly.david.data.room.history.search.SearchHistoryRoomModel
import ly.david.ui.common.paging.insertFooterItemForNonEmpty

private const val SEARCH_DELAY_MS = 500L

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val searchHistoryDao: SearchHistoryDao
) : ViewModel() {

    private data class ViewModelState(
        val query: String,
        val entity: MusicBrainzResource,
    )

    val searchQuery = MutableStateFlow("")
    val searchEntity = MutableStateFlow(MusicBrainzResource.ARTIST)
    private val viewModelState = combine(searchQuery, searchEntity) { query, entity ->
        ViewModelState(query, entity)
    }

    fun search(query: String? = null, entity: MusicBrainzResource? = null) {
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
        viewModelScope.launch {
            val query = searchQuery.value
            if (query.isEmpty()) return@launch
            val entity = searchEntity.value
            searchHistoryDao.insertReplace(
                SearchHistoryRoomModel(
                    id = "${entity}_$query",
                    query = query,
                    entity = entity
                )
            )
        }
    }

    fun deleteSearchHistoryItem(item: SearchHistoryListItemModel) {
        viewModelScope.launch {
            searchHistoryDao.delete(
                query = item.query,
                entity = item.entity
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<ListItemModel>> =
        viewModelState.filterNot { it.query.isEmpty() }
            .debounce(SEARCH_DELAY_MS)
            .flatMapLatest { viewModelState ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    pagingSourceFactory = {
                        SearchMusicBrainzPagingSource(
                            searchApi = musicBrainzApiService,
                            resource = viewModelState.entity,
                            queryString = viewModelState.query,
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData.insertFooterItemForNonEmpty(item = EndOfList)
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchHistory: Flow<PagingData<ListItemModel>> =
        viewModelState.filter { it.query.isEmpty() }
            .flatMapLatest { viewModelState ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    pagingSourceFactory = {
                        searchHistoryDao.getAllSearchHistory(
                            query = "%${viewModelState.query}%",
                            entity = viewModelState.entity
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toSearchHistoryListItemModel()
                    }.insertSeparators { before: SearchHistoryListItemModel?, _: SearchHistoryListItemModel? ->
                        if (before == null) Header else null
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
