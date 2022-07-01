package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.mbjc.data.domain.Header
import ly.david.mbjc.data.domain.RelationUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.domain.toRelationUiModel
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.recording.RecordingRelationDao
import ly.david.mbjc.ui.common.paging.MusicBrainzPagingConfig

@HiltViewModel
internal class RecordingViewModel @Inject constructor(
    private val recordingRepository: RecordingRepository,
    private val recordingRelationDao: RecordingRelationDao
) : ViewModel() {

    private val recordingId: MutableStateFlow<String> = MutableStateFlow("")

    suspend fun lookupRecording(recordingId: String) = recordingRepository.lookupRecording(recordingId).also {
        this.recordingId.value = it.id
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedRelations: Flow<PagingData<UiModel>> =
        recordingId.flatMapLatest { recordingId ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    recordingRelationDao.getRelationsForRecording(recordingId, MusicBrainzResource.RECORDING)
                }
            ).flow.map { pagingData ->
                pagingData.map { relation ->
                    relation.toRelationUiModel()
                }.insertSeparators { before: RelationUiModel?, _: RelationUiModel? ->
                    if (before == null) Header else null
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
