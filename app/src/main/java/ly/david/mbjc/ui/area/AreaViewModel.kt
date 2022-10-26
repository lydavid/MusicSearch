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
import ly.david.data.domain.AreaUiModel
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.toReleaseUiModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.repository.AreaRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
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

    // TODO: we're doing a lookup with "" areaid
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
        return repository.lookupArea(
            areaId = areaId,
            hasRelationsBeenStored = { hasRelationsBeenStored() },
            // TODO: need to include this check, or we will not query for relations when coming from Release's Details
            markResourceHasRelations = { markResourceHasRelations() }
        )
            .also {
                loadRelations(areaId)
            }
    }

    override suspend fun lookupRelationsAndStore(resourceId: String, forceRefresh: Boolean) {
        repository.lookupArea(
            areaId = resourceId,
            forceRefresh = forceRefresh,
            hasRelationsBeenStored = { hasRelationsBeenStored() },
            markResourceHasRelations = { markResourceHasRelations() }
        )
    }

    @OptIn(ExperimentalPagingApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val pagedReleases: Flow<PagingData<ReleaseUiModel>> =
        paramState.filterNot { it.areaId.isEmpty() }
            .flatMapLatest { (areaId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { repository.getRemoteReleasesByAreaCount(areaId) },
                        getLocalResourceCount = { repository.getLocalReleasesByAreaCount(areaId) },
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
