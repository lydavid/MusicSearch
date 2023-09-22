package ly.david.mbjc.ui.release.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.core.common.transformThisIfNotNullOrEmpty
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.ListSeparator
import ly.david.data.domain.listitem.TrackListItemModel
import ly.david.data.domain.listitem.toTrackListItemModel
import ly.david.data.domain.paging.LookupEntityRemoteMediator
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.domain.release.ReleaseRepository
import ly.david.data.room.release.RoomReleaseDao
import ly.david.data.room.release.tracks.MediumRoomModel
import ly.david.data.room.release.tracks.RoomMediumDao
import ly.david.data.room.release.tracks.RoomTrackDao
import ly.david.data.room.release.tracks.TrackForListItem
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class TracksByReleaseViewModel(
    private val repository: ReleaseRepository,
    private val releaseDao: RoomReleaseDao,
    private val mediumDao: RoomMediumDao,
    private val trackDao: RoomTrackDao,
) : ViewModel() {

    private data class ViewModelState(
        val releaseId: String = "",
        val query: String = "",
    )

    private val releaseId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val tracksParamState: Flow<ViewModelState> = combine(releaseId, query) { releaseId, query ->
        ViewModelState(releaseId, query)
    }.distinctUntilChanged()

    fun loadTracks(releaseId: String) {
        this.releaseId.value = releaseId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    val pagedTracks: Flow<PagingData<ListItemModel>> =
        tracksParamState.filterNot { it.releaseId.isEmpty() }
            .flatMapLatest { (releaseId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = LookupEntityRemoteMediator(
                        hasEntityBeenStored = { hasReleaseTracksBeenStored(releaseId) },
                        lookupEntity = { repository.lookupRelease(releaseId) },
                        deleteLocalEntity = { releaseDao.deleteReleaseById(releaseId) }
                    ),
                    pagingSourceFactory = {
                        trackDao.getTracksByRelease(
                            releaseId = releaseId,
                            query = "%$query%",
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData
                        .map(TrackForListItem::toTrackListItemModel)
                        .insertSeparators { before: TrackListItemModel?, after: TrackListItemModel? ->
                            if (before?.mediumId != after?.mediumId && after != null) {
                                val medium: MediumRoomModel =
                                    mediumDao.getMediumForTrack(after.id) ?: return@insertSeparators null

                                ListSeparator(
                                    id = "${medium.id}",
                                    text = medium.format.orEmpty() +
                                        (medium.position?.toString() ?: "").transformThisIfNotNullOrEmpty { " $it" } +
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

    private suspend fun hasReleaseTracksBeenStored(releaseId: String): Boolean {
        val roomRelease = releaseDao.getReleaseWithFormatTrackCounts(releaseId)
        return !roomRelease?.formatTrackCounts.isNullOrEmpty()
    }
}
