package ly.david.mbjc.ui.area

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
import ly.david.mbjc.data.domain.AreaUiModel
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.toReleaseUiModel
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.repository.AreaRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.BrowseResourceRemoteMediator
import ly.david.mbjc.ui.common.paging.MusicBrainzPagingConfig
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class AreaViewModel @Inject constructor(
    private val repository: AreaRepository,
    relationDao: RelationDao,
    override val lookupHistoryDao: LookupHistoryDao
) : RelationViewModel(relationDao), RecordLookupHistory {

    private data class ViewModelState(
        val areaId: String = "",
        val query: String = ""
    )

    private val areaId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(areaId, query) { areaId, query ->
        ViewModelState(areaId, query)
    }.distinctUntilChanged()

    fun loadReleases(areaId: String) {
        this.areaId.value = areaId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    /**
     * Call this to retrieve title, and initiate relations paging.
     */
    suspend fun lookupAreaThenLoadRelations(areaId: String): AreaUiModel {
        return repository.lookupArea(areaId)
            .also {
                markResourceHasRelations()
                loadRelations(areaId)
            }
    }

    override suspend fun lookupRelationsAndStore(resourceId: String, forceRefresh: Boolean) {
        repository.lookupArea(resourceId, forceRefresh = forceRefresh)
//        markResourceHasRelations()
    }

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
