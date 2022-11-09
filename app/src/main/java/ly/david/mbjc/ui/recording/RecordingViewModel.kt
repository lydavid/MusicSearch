package ly.david.mbjc.ui.recording

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.repository.RecordingRepository
import ly.david.mbjc.ui.relation.RelationViewModel
import ly.david.mbjc.ui.release.IReleasesList
import ly.david.mbjc.ui.release.ReleasesList

@HiltViewModel
internal class RecordingViewModel @Inject constructor(
    private val repository: RecordingRepository,
    relationDao: RelationDao,
    private val releasesList: ReleasesList
) : RelationViewModel(relationDao), IReleasesList by releasesList {

    init {
        releasesList.scope = viewModelScope
        releasesList.repository = repository
    }

    suspend fun lookupRecording(recordingId: String) = repository.lookupRecording(recordingId).also {
        loadRelations(it.id)
    }
}
