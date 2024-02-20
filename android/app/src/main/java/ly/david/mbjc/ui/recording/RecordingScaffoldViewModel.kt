package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.core.models.artist.getDisplayNames
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.recording.RecordingScaffoldModel
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.recording.RecordingRepository
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class RecordingScaffoldViewModel(
    private val repository: RecordingRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
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
        relationsList.entity = entity
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
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = recordingId,
                                title = title.value,
                                entity = entity,
                            ),
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
