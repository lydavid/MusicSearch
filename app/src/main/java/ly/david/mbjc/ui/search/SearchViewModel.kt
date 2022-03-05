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
import ly.david.mbjc.data.Artist
import ly.david.mbjc.preferences.INITIAL_SEARCH_LIMIT
import ly.david.mbjc.preferences.SEARCH_LIMIT

internal class SearchViewModel : ViewModel() {

    val query: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val artists: Flow<PagingData<Artist>> =
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
                ).flow
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
