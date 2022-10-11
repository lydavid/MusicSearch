package ly.david.mbjc.ui.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.toReleaseUiModel
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.BrowseResourceRemoteMediator
import ly.david.mbjc.ui.common.paging.MusicBrainzPagingConfig

@HiltViewModel
internal class AreaViewModel @Inject constructor(
    private val repository: AreaRepository,
    override val lookupHistoryDao: LookupHistoryDao
) : ViewModel(), RecordLookupHistory {

    private data class ViewModelState(
        val areaId: String = "",
        val query: String = ""
    )

    val areaId: MutableStateFlow<String> = MutableStateFlow("")
    val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(areaId, query) { areaId, query ->
        ViewModelState(areaId, query)
    }.distinctUntilChanged()

    fun updateAreaId(areaId: String) {
        this.areaId.value = areaId
    }

//    suspend fun lookupArea(areaId: String): Area =
//        repository.lookupArea(areaId)

    @OptIn(ExperimentalPagingApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val pagedReleases: Flow<PagingData<ReleaseUiModel>> =
        paramState.filterNot { it.areaId.isEmpty() }
            .flatMapLatest { (areaId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { repository.getTotalReleases(areaId) },
                        getLocalResourceCount = { repository.getNumberOfReleasesByArea(areaId) },
                        deleteLocalResource = { repository.deleteReleasesByArea(areaId) },
                        browseResource = { offset ->
                            repository.browseReleasesAndStore(areaId, offset)
                        }
                    ),
                    pagingSourceFactory = { repository.getReleasesPagingSource(areaId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toReleaseUiModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
