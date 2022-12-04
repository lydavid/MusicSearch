package ly.david.mbjc.ui.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.AreaScaffoldModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.AreaRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList
import ly.david.mbjc.ui.release.IReleasesList
import ly.david.mbjc.ui.release.ReleasesList

/**
 * The logic for loading relations is different here because we start on relationships tab.
 * We cannot start on releases tab because non-country areas do not have releases.
 */
@HiltViewModel
internal class AreaViewModel @Inject constructor(
    private val repository: AreaRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
    private val releasesList: ReleasesList
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList,
    IReleasesList by releasesList {

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository

        releasesList.scope = viewModelScope
        releasesList.repository = repository
    }

    /**
     * Call this to retrieve title, and initiate relations paging.
     */
    suspend fun lookupAreaThenLoadRelations(areaId: String): AreaScaffoldModel {
        return repository.lookupArea(
            areaId = areaId,
            hasRelationsBeenStored = { hasRelationsBeenStored() },
            // Need to include this check, or we will not query for relations when coming from Release's Details
            markResourceHasRelations = { markResourceHasRelations() }
        ).also {
            loadRelations(areaId)
        }
    }
}
