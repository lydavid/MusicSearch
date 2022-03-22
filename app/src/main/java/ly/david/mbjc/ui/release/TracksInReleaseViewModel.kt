package ly.david.mbjc.ui.release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.mbjc.data.domain.sub.UiTrack
import ly.david.mbjc.data.domain.sub.toUiTrack
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.TrackDao

@HiltViewModel
class TracksInReleaseViewModel @Inject constructor(
    private val releaseRepository: ReleaseRepository,
//    private val musicBrainzApiService: MusicBrainzApiService,
//    private val lookupHistoryDao: LookupHistoryDao
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
) : ViewModel() {

    private val releaseId: MutableStateFlow<String> = MutableStateFlow("")

    suspend fun lookupRelease(releaseId: String) = releaseRepository.lookupRelease(releaseId).also {
        this.releaseId.value = it.id
    }

//    suspend fun insertMediaAndTracks(musicBrainzRelease: MusicBrainzRelease) {
//        musicBrainzRelease.media?.forEach { medium ->
//            val mediumId = mediumDao.insert(medium.toRoomMedium(musicBrainzRelease.id))
//            trackDao.insertAll(medium.tracks?.map { it.toRoomTrack(mediumId) } ?: emptyList())
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedTracks: Flow<PagingData<UiTrack>> =
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
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

//    private var musicBrainzRelease: MusicBrainzRelease? = null
//
//    suspend fun lookupRelease(releaseId: String, ): MusicBrainzRelease =
//        musicBrainzRelease ?: musicBrainzApiService.lookupRelease(releaseId).also {
//            // TODO: insert release into table or we can do it when getting all release in release group
//            //  only artist lookup needed to do it here since we didn't cache the query results
//            incrementOrInsertLookupHistory(it)
//            musicBrainzRelease = it
//        }
//
//    // TODO: see if we can generalize
//    private suspend fun incrementOrInsertLookupHistory(musicBrainzRelease: MusicBrainzRelease) {
//        lookupHistoryDao.incrementOrInsertLookupHistory(
//            LookupHistory(
//                summary = musicBrainzRelease.getNameWithDisambiguation(),
//                destination = Destination.LOOKUP_RELEASE,
//                mbid = musicBrainzRelease.id
//            )
//        )
//    }
}
