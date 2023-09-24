package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.getDisplayNames
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.history.IncrementLookupHistoryUseCase
import ly.david.data.domain.recordng.RecordingRepository
import ly.david.data.domain.recordng.RecordingScaffoldModel
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class RecordingScaffoldViewModel(
    private val repository: RecordingRepository,
    private val incrementLookupHistoryUseCase: IncrementLookupHistoryUseCase,
    private val relationsList: RelationsList,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.RECORDING
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val subtitle = MutableStateFlow("")
    val recording: MutableStateFlow<RecordingScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
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
                        incrementLookupHistoryUseCase(
                            LookupHistory(
                                mbid = recordingId,
                                title = title.value,
                                entity = entity,
                            )
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
