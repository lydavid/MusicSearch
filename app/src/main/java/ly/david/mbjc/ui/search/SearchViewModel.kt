package ly.david.mbjc.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.domain.listitem.EndOfList
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.domain.paging.SearchMusicBrainzPagingSource
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.history.search.SearchHistoryDao
import ly.david.data.room.history.search.SearchHistoryRoomModel
import ly.david.ui.common.paging.insertFooterItemForNonEmpty

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val searchHistoryDao: SearchHistoryDao
) : ViewModel() {

    private data class ViewModelState(
        val query: String = "",
        val entity: MusicBrainzResource = MusicBrainzResource.ARTIST,
    )

    private val viewModelState = MutableStateFlow(ViewModelState())

    fun search(query: String, entity: MusicBrainzResource) {
        viewModelState.value = ViewModelState(query, entity)
        viewModelScope.launch {
            searchHistoryDao.insertReplace(
                SearchHistoryRoomModel(
                    id = "${entity}_$query",
                    query = query,
                    entity = entity
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResultsListItemModel: Flow<PagingData<ListItemModel>> =
        viewModelState.filterNot { it.query.isEmpty() }
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
}
