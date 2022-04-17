package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class RecordingViewModel @Inject constructor(
    private val recordingRepository: RecordingRepository
) : ViewModel() {
    suspend fun lookupRecording(recordingId: String) = recordingRepository.lookupRecording(recordingId)
}
