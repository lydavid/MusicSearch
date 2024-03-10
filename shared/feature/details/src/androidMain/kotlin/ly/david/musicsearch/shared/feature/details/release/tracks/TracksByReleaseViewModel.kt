package ly.david.musicsearch.shared.feature.details.release.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.domain.release.usecase.GetTracksByRelease
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class TracksByReleaseViewModel(
    private val getTracksByRelease: GetTracksByRelease,
) : ViewModel() {

    private data class ViewModelState(
        val releaseId: String = "",
        val query: String = "",
    )

    private val releaseId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val tracksParamState: Flow<ViewModelState> = combine(
        releaseId,
        query,
    ) { releaseId, query ->
        ViewModelState(
            releaseId,
            query,
        )
    }.distinctUntilChanged()

    fun loadTracks(releaseId: String) {
        this.releaseId.value = releaseId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(
        ExperimentalCoroutinesApi::class,
    )
    val pagedTracks: Flow<PagingData<ListItemModel>> =
        tracksParamState.filterNot { it.releaseId.isEmpty() }
            .flatMapLatest { (releaseId, query) ->
                getTracksByRelease(
                    releaseId = releaseId,
                    query = query,
                )
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
