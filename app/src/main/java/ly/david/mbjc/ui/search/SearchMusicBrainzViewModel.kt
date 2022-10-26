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
import ly.david.data.domain.UiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.paging.SearchMusicBrainzPagingSource
import ly.david.mbjc.ui.common.paging.insertFooterItemForNonEmpty

@HiltViewModel
internal class SearchMusicBrainzViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService
) : ViewModel() {

    private data class ViewModelState(
        val resource: MusicBrainzResource = MusicBrainzResource.ARTIST,
        val query: String = "",
    )

    private val viewModelState = MutableStateFlow(ViewModelState())

    fun updateViewModelState(resource: MusicBrainzResource, query: String) {
        viewModelState.value = ViewModelState(resource, query)
    }

    // TODO: refresh not implemented
    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResultsUiModel: Flow<PagingData<UiModel>> =
        viewModelState.filterNot { it.query.isEmpty() }
            .flatMapLatest { viewModelState ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    pagingSourceFactory = {
                        SearchMusicBrainzPagingSource(
                            searchApi = musicBrainzApiService,
                            resource = viewModelState.resource,
                            queryString = viewModelState.query,
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData.insertFooterItemForNonEmpty(item = ly.david.data.domain.EndOfList)
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
