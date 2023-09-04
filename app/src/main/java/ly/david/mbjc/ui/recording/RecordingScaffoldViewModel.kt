package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.recordng.RecordingRepository
import ly.david.data.domain.recordng.RecordingScaffoldModel
import ly.david.data.core.getDisplayNames
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import timber.log.Timber

@HiltViewModel
internal class RecordingScaffoldViewModel @Inject constructor(
    private val repository: RecordingRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.RECORDING
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val subtitle = MutableStateFlow("")
    val recording: MutableStateFlow<RecordingScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun loadDataForTab(
        recordingId: String,
        selectedTab: RecordingTab,
    ) {
        when (selectedTab) {
            RecordingTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val recordingScaffoldModel = repository.lookupRecording(recordingId)
                        if (title.value.isEmpty()) {
                            title.value = recordingScaffoldModel.getNameWithDisambiguation()
                        }
                        subtitle.value = "Recording by ${recordingScaffoldModel.artistCredits.getDisplayNames()}"
                        recording.value = recordingScaffoldModel
                        isError.value = false
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            entityId = recordingId,
                            entity = entity,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            RecordingTab.RELATIONSHIPS -> loadRelations(recordingId)
            else -> {
                // Not handled here.
            }
        }
    }
}
