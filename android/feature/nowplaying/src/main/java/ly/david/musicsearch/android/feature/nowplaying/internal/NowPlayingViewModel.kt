package ly.david.musicsearch.android.feature.nowplaying.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.domain.nowplaying.usecase.DeleteNowPlayingHistory
import ly.david.musicsearch.domain.nowplaying.usecase.GetNowPlayingHistory
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class NowPlayingViewModel(
    private val getNowPlayingHistory: GetNowPlayingHistory,
    private val deleteNowPlayingHistory: DeleteNowPlayingHistory,
) : ViewModel() {

    private val query = MutableStateFlow("")

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val nowPlayingHistory: Flow<PagingData<ListItemModel>> =
        query.flatMapLatest { query ->
            getNowPlayingHistory(
                query = query,
            )
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    fun delete(id: String) {
        deleteNowPlayingHistory(id)
    }
}
