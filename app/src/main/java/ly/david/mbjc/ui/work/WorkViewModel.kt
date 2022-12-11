package ly.david.mbjc.ui.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.WorkListItemModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.WorkRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.PagedList
import ly.david.mbjc.ui.recording.RecordingsPagedList
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList

@HiltViewModel
internal class WorkViewModel @Inject constructor(
    private val repository: WorkRepository,
    private val relationsList: RelationsList,
    private val recordingsPagedList: RecordingsPagedList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList,
    PagedList<RecordingListItemModel> by recordingsPagedList {

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository

        recordingsPagedList.scope = viewModelScope
        recordingsPagedList.useCase = repository
    }

    suspend fun lookupWork(workId: String): WorkListItemModel =
        repository.lookupWork(workId)
}
