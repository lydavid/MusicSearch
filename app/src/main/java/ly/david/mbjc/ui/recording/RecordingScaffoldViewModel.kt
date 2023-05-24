package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.RecordingScaffoldModel
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.RecordingRepository
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.data.persistence.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
internal class RecordingScaffoldViewModel @Inject constructor(
    private val repository: RecordingRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.RECORDING
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
        selectedTab: RecordingTab
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
                    } catch (ex: HttpException) {
                        Timber.e(ex)
                        isError.value = true
                    } catch (ex: IOException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = recordingId,
                            resource = resource,
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
