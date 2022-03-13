package ly.david.mbjc.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.mbjc.data.EndOfList
import ly.david.mbjc.data.UiData
import ly.david.mbjc.data.network.INITIAL_SEARCH_LIMIT
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.SEARCH_LIMIT
import ly.david.mbjc.ui.common.paging.insertFooterItemForNonEmpty

internal class SearchMusicBrainzViewModel : ViewModel() {

    private data class ViewModelState(
        val resource: MusicBrainzResource = MusicBrainzResource.ARTIST,
        val query: String = "",
    )

    private val viewModelState = MutableStateFlow(ViewModelState())

    fun updateViewModelState(resource: MusicBrainzResource, query: String) {
        viewModelState.value = ViewModelState(resource, query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResultsUiData: Flow<PagingData<UiData>> =
        viewModelState.filterNot { it.query.isEmpty() }
            .flatMapLatest { viewModelState ->
                Pager(
                    config = PagingConfig(
                        pageSize = SEARCH_LIMIT,
                        initialLoadSize = INITIAL_SEARCH_LIMIT
                    ),
                    pagingSourceFactory = {
                        SearchMusicBrainzPagingSource(
                            resource = viewModelState.resource,
                            queryString = viewModelState.query,
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData.insertFooterItemForNonEmpty(item = EndOfList)
                    // TODO: can we somehow insert a footer for when we know there are more results but network failed?
                    // TODO: loading more footer?
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
