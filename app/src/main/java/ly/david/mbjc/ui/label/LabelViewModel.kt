package ly.david.mbjc.ui.label

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
import ly.david.data.domain.LabelUiModel
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.toReleaseUiModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.LabelRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.MusicBrainzPagingConfig

@HiltViewModel
internal class LabelViewModel @Inject constructor(
    private val repository: LabelRepository,
    override val lookupHistoryDao: LookupHistoryDao
) : ViewModel(), RecordLookupHistory {

    private data class ViewModelState(
        val labelId: String = "",
        val query: String = ""
    )

    val labelId: MutableStateFlow<String> = MutableStateFlow("")
    val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(labelId, query) { labelId, query ->
        ViewModelState(labelId, query)
    }.distinctUntilChanged()

    suspend fun lookupLabel(labelId: String): LabelUiModel =
        repository.lookupLabel(labelId)

    @OptIn(ExperimentalPagingApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val pagedReleases: Flow<PagingData<ReleaseUiModel>> =
        paramState.filterNot { it.labelId.isEmpty() }
            .flatMapLatest { (labelId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { repository.getTotalReleases(labelId) },
                        getLocalResourceCount = { repository.getNumberOfReleasesByLabel(labelId) },
                        deleteLocalResource = { repository.deleteReleasesByLabel(labelId) },
                        browseResource = { offset ->
                            repository.browseReleasesAndStore(labelId, offset)
                        }
                    ),
                    pagingSourceFactory = { repository.getReleasesPagingSource(labelId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toReleaseUiModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
