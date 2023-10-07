package ly.david.musicsearch.feature.search.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
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
import ly.david.data.core.history.SearchHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.SearchHistoryDao
import ly.david.musicsearch.domain.listitem.EndOfList
import ly.david.musicsearch.domain.listitem.Header
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.musicsearch.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.domain.listitem.toSearchHistoryListItemModel
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig
import ly.david.musicsearch.domain.paging.SearchMusicBrainzPagingSource
import ly.david.ui.common.paging.insertFooterItemForNonEmpty

private const val SEARCH_DELAY_MS = 500L

internal class SearchViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val searchHistoryDao: SearchHistoryDao,
) : ViewModel() {

    private data class ViewModelState(
        val query: String,
        val entity: MusicBrainzEntity,
    )

    val searchQuery = MutableStateFlow("")
    val searchEntity = MutableStateFlow(MusicBrainzEntity.ARTIST)
    private val viewModelState = combine(searchQuery, searchEntity) { query, entity ->
        ViewModelState(query, entity)
    }

    fun search(query: String? = null, entity: MusicBrainzEntity? = null) {
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
        if (query.isEmpty()) return
        val entity = searchEntity.value
        searchHistoryDao.upsert(
            SearchHistory(
                entity = entity,
                query = query,
            )
        )
    }

    fun deleteSearchHistoryItem(item: SearchHistoryListItemModel) {
        searchHistoryDao.delete(
            entity = item.entity,
            query = item.query,
        )
    }

    fun deleteAllSearchHistoryForEntity() {
        searchHistoryDao.deleteAll(searchEntity.value)
    }

    // TODO: because of debounce, scrollToItem(0) does not work properly
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<ListItemModel>> =
        viewModelState.filterNot { it.query.isEmpty() }
            .debounce(SEARCH_DELAY_MS)
            .flatMapLatest { viewModelState ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    pagingSourceFactory = {
                        SearchMusicBrainzPagingSource(
                            searchApi = musicBrainzApi,
                            entity = viewModelState.entity,
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
                            entity = viewModelState.entity
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData
                        .map(SearchHistory::toSearchHistoryListItemModel)
                        .insertSeparators { before: SearchHistoryListItemModel?, after: SearchHistoryListItemModel? ->
                            if (before == null) Header() else null
                        }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
