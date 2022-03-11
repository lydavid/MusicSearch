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
import ly.david.mbjc.data.network.INITIAL_SEARCH_LIMIT
import ly.david.mbjc.data.network.SEARCH_LIMIT
import ly.david.mbjc.ui.common.paging.insertFooterItemForNonEmpty

internal class SearchArtistsViewModel : ViewModel() {

    val query: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val artists: Flow<PagingData<SearchArtistsUiModel>> =
        query.filterNot { it.isEmpty() }
            .flatMapLatest { query ->
                Pager(
                    config = PagingConfig(
                        pageSize = SEARCH_LIMIT,
                        initialLoadSize = INITIAL_SEARCH_LIMIT
                    ),
                    pagingSourceFactory = {
                        SearchArtistsPagingSource(queryString = query)
                    }
                ).flow.map { pagingData ->
                    // TODO: can we somehow insert a footer for when we know there are more results but network failed?
                    pagingData.insertFooterItemForNonEmpty(item = SearchArtistsUiModel.EndOfList)
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
