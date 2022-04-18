package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.mbjc.data.persistence.RelationDao

@HiltViewModel
internal class RecordingViewModel @Inject constructor(
    private val recordingRepository: RecordingRepository,
    private val relationDao: RelationDao
) : ViewModel() {

    private val recordingId: MutableStateFlow<String> = MutableStateFlow("")

    suspend fun lookupRecording(recordingId: String) = recordingRepository.lookupRecording(recordingId).also {
        this.recordingId.value = it.id
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val pagedRelations: Flow<PagingData<UiModel>> =
//        releaseId.flatMapLatest { releaseId ->
//            Pager(
//                config = MusicBrainzPagingConfig.pagingConfig,
//                pagingSourceFactory = {
//                    trackDao.getTracksInRelease(releaseId)
//                }
//            ).flow.map { pagingData ->
//                pagingData.map { track ->
//                    track.toTrackUiModel()
//                }.insertSeparators { _: TrackUiModel?, after: TrackUiModel? ->
//                    // TODO: if we want separators when we filter, then we should compare before/after medium id
//                    //  before converting it to uitrack...
//                    if (after?.position == 1) {
//                        val medium: MediumRoomModel = mediumDao.getMediumForTrack(after.id)
//                        ListSeparator(
//                            text = "${medium.format.orEmpty()} ${medium.position}" +
//                                medium.title.transformThisIfNotNullOrEmpty { " ($it)" }
//                        )
//                    } else {
//                        null
//                    }
//                }
//            }
//        }
//            .distinctUntilChanged()
//            .cachedIn(viewModelScope)
}
