package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.RecordingRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList
import ly.david.mbjc.ui.common.paging.PagedList
import ly.david.mbjc.ui.release.ReleasesPagedList

@HiltViewModel
internal class RecordingViewModel @Inject constructor(
    private val repository: RecordingRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val releasesPagedList: ReleasesPagedList,
    private val relationsList: RelationsList,
) : ViewModel(), RecordLookupHistory,
    PagedList<ReleaseListItemModel> by releasesPagedList,
    IRelationsList by relationsList {

    init {
        releasesPagedList.scope = viewModelScope
        releasesPagedList.repository = repository

        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    suspend fun lookupRecording(recordingId: String) =
        repository.lookupRecording(recordingId)
}
