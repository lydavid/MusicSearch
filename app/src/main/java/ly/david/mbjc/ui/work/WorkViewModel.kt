package ly.david.mbjc.ui.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.WorkListItemModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.WorkRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.recording.IRecordingsList
import ly.david.mbjc.ui.recording.RecordingsList
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList

@HiltViewModel
internal class WorkViewModel @Inject constructor(
    private val repository: WorkRepository,
    private val relationsList: RelationsList,
    private val recordingsList: RecordingsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList,
    IRecordingsList by recordingsList {

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository

        recordingsList.scope = viewModelScope
        recordingsList.repository = repository
    }

    suspend fun lookupWorkThenLoadRelations(workId: String): WorkListItemModel {
        return repository.lookupWork(
            workId = workId,
            hasRelationsBeenStored = { hasRelationsBeenStored() },
            markResourceHasRelations = { markResourceHasRelations() }
        ).also {
            loadRelations(it.id)
        }
    }
}
