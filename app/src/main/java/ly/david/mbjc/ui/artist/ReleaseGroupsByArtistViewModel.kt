package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.mbjc.data.domain.ListSeparator
import ly.david.mbjc.data.domain.UiData
import ly.david.mbjc.data.domain.UiReleaseGroup
import ly.david.mbjc.data.domain.toUiReleaseGroup
import ly.david.mbjc.data.getDisplayTypes
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.getRoomReleaseGroupArtistCredit
import ly.david.mbjc.data.persistence.ArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupDao
import ly.david.mbjc.data.persistence.toRoomReleaseGroup
import ly.david.mbjc.ui.common.paging.RoomDataRemoteMediator

@HiltViewModel
class ReleaseGroupsByArtistViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistRepository: ArtistRepository,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao,
    private val artistDao: ArtistDao,
) : ViewModel() {

    private data class ViewModelState(
        val artistId: String = "",
        val query: String = "",
        val isSorted: Boolean = false
    )

    private val artistId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val isSorted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val paramState = combine(artistId, query, isSorted) { artistId, query, isSorted ->
        ViewModelState(artistId, query, isSorted)
    }.distinctUntilChanged()

    suspend fun lookupArtist(artistId: String) =
        artistRepository.lookupArtist(artistId)

    fun updateArtistId(artistId: String) {
        this.artistId.value = artistId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun updateIsSorted(isSorted: Boolean) {
        this.isSorted.value = isSorted
    }

    @OptIn(ExperimentalPagingApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val pagedReleaseGroups: Flow<PagingData<UiData>> =
        paramState.filterNot { it.artistId.isEmpty() }
            .flatMapLatest { (artistId, query, isSorted) ->
                Pager(
                    config = PagingConfig(
                        pageSize = BROWSE_LIMIT
                    ),
                    remoteMediator = RoomDataRemoteMediator(
                        getRemoteResourceCount = { artistDao.getArtist(artistId)?.releaseGroupsCount },
                        getLocalResourceCount = { releaseGroupDao.getNumberOfReleaseGroupsByArtist(artistId) },
                        browseResource = { offset ->
                            browseReleaseGroupsAndStore(artistId, offset)
                        }
                    ),
                    pagingSourceFactory = { getPagingSource(artistId, query, isSorted) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toUiReleaseGroup(releaseGroupArtistDao.getReleaseGroupArtistCredits(it.id))
                    }.insertSeparators { uiReleaseGroup: UiReleaseGroup?, uiReleaseGroup2: UiReleaseGroup? ->
                        if (isSorted && uiReleaseGroup2 != null &&
                            (uiReleaseGroup?.primaryType != uiReleaseGroup2.primaryType ||
                                uiReleaseGroup?.secondaryTypes != uiReleaseGroup2.secondaryTypes)
                        ) {
                            ListSeparator(uiReleaseGroup2.getDisplayTypes())
                        } else {
                            null
                        }
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    private suspend fun browseReleaseGroupsAndStore(artistId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleaseGroupsByArtist(
            artistId = artistId,
            limit = BROWSE_LIMIT,
            offset = nextOffset
        )

        // Only need to update it the first time we ever browse this artist's release groups.
        if (response.offset == 0) {
            artistDao.setReleaseGroupCount(artistId, response.count)
        }

        val musicBrainzReleaseGroups = response.releaseGroups

        releaseGroupDao.insertAll(musicBrainzReleaseGroups.map { it.toRoomReleaseGroup() })
        releaseGroupArtistDao.insertAll(
            musicBrainzReleaseGroups.flatMap { releaseGroup ->
                releaseGroup.getRoomReleaseGroupArtistCredit()
            }
        )

        return musicBrainzReleaseGroups.size
    }

    private fun getPagingSource(artistId: String, query: String, isSorted: Boolean) = when {
        isSorted && query.isEmpty() -> {
            releaseGroupDao.getReleaseGroupsByArtistSorted(artistId)
        }
        isSorted -> {
            releaseGroupDao.getReleaseGroupsByArtistFilteredSorted(
                artistId = artistId,
                query = "%$query%"
            )
        }
        query.isEmpty() -> {
            releaseGroupDao.getReleaseGroupsByArtist(artistId)
        }
        else -> {
            releaseGroupDao.getReleaseGroupsByArtistFiltered(
                artistId = artistId,
                query = "%$query%"
            )
        }
    }
}
