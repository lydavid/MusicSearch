package ly.david.mbjc.ui.release

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
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
import ly.david.mbjc.data.domain.ListSeparator
import ly.david.mbjc.data.domain.UiData
import ly.david.mbjc.data.domain.UiTrack
import ly.david.mbjc.data.domain.toUiTrack
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.RoomMedium
import ly.david.mbjc.data.persistence.release.TrackDao
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

@HiltViewModel
class TracksInReleaseViewModel @Inject constructor(
    private val releaseRepository: ReleaseRepository,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
) : ViewModel() {

    private val releaseId: MutableStateFlow<String> = MutableStateFlow("")

    suspend fun lookupRelease(releaseId: String) = releaseRepository.lookupRelease(releaseId).also {
        this.releaseId.value = it.id
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedTracks: Flow<PagingData<UiData>> =
        releaseId.flatMapLatest { releaseId ->
            Pager(
                config = PagingConfig(
                    pageSize = BROWSE_LIMIT,
                ),
                pagingSourceFactory = {
                    trackDao.getTracksInRelease(releaseId)
                }
            ).flow.map { pagingData ->
                pagingData.map { track ->
                    track.toUiTrack()
                }.insertSeparators { _: UiTrack?, after: UiTrack? ->
                    // TODO: if we want separators when we filter, then we should compare before/after medium id
                    //  before converting it to uitrack...
                    if (after?.position == 1) {
                        val medium: RoomMedium = mediumDao.getMediumForTrack(after.id)
                        Log.d("Remove This", "$medium: ")
                        ListSeparator(
                            text = "${medium.format.orEmpty()} ${medium.position}" +
                                medium.title.transformThisIfNotNullOrEmpty { " ($it)" }
                        )
                    } else {
                        null
                    }
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
