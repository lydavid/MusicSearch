package ly.david.mbjc.ui.recording

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class RecordingViewModel @Inject constructor(
    private val recordingRepository: RecordingRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupRecording(recordingId: String) = recordingRepository.lookupRecording(recordingId).also {
        this.resourceId.value = it.id
    }
}
