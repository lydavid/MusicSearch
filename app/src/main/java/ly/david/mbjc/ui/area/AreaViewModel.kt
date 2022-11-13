package ly.david.mbjc.ui.area

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.AreaUiModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.repository.AreaRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.RelationViewModel
import ly.david.mbjc.ui.release.IReleasesList
import ly.david.mbjc.ui.release.ReleasesList

@HiltViewModel
internal class AreaViewModel @Inject constructor(
    private val repository: AreaRepository,
    relationDao: RelationDao,
    override val lookupHistoryDao: LookupHistoryDao,
    private val releasesList: ReleasesList
) : RelationViewModel(relationDao), RecordLookupHistory,
    IReleasesList by releasesList {

    init {
        releasesList.scope = viewModelScope
        releasesList.repository = repository
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
}
