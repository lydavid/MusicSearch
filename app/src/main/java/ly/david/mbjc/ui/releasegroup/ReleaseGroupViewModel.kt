package ly.david.mbjc.ui.releasegroup

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
import ly.david.data.domain.ReleaseGroupUiModel
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.toReleaseUiModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.ReleaseGroupRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory

// TODO: generalize? reuse for releases by label
//  or make abstract, and override

// TODO: for releases by release group, and releases by label, we can just literally use the same VM/screen
//  but change out what id to use, and what daos to use
@HiltViewModel
internal class ReleaseGroupViewModel @Inject constructor(
    private val repository: ReleaseGroupRepository,
    override val lookupHistoryDao: LookupHistoryDao
) : ViewModel(), RecordLookupHistory {

    private data class ViewModelState(
        val releaseGroupId: String = "",
        val query: String = ""
    )

    private val releaseGroupId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(releaseGroupId, query) { releaseGroupId, query ->
        ViewModelState(releaseGroupId, query)
    }.distinctUntilChanged()

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupUiModel =
        repository.lookupReleaseGroup(releaseGroupId)

    fun updateReleaseGroupId(releaseGroupId: String) {
        this.releaseGroupId.value = releaseGroupId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalPagingApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val pagedReleases: Flow<PagingData<ReleaseUiModel>> =
        paramState.filterNot { it.releaseGroupId.isEmpty() }
            .flatMapLatest { (releaseGroupId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { repository.getRemoteReleasesByReleaseGroupCount(releaseGroupId) },
                        getLocalResourceCount = { repository.getLocalReleasesByReleaseGroupCount(releaseGroupId) },
                        deleteLocalResource = { repository.deleteReleasesByReleaseGroup(releaseGroupId) },
                        browseResource = { offset ->
                            repository.browseReleasesAndStore(releaseGroupId, offset)
                        }
                    ),
                    pagingSourceFactory = { repository.getPagingSource(releaseGroupId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toReleaseUiModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
