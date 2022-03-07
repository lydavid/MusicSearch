package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import ly.david.mbjc.data.UiReleaseGroup
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.network.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzReleaseGroup
import ly.david.mbjc.data.persistence.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupDao
import ly.david.mbjc.data.persistence.RoomReleaseGroup
import ly.david.mbjc.data.persistence.RoomReleaseGroupArtistCredit
import ly.david.mbjc.data.persistence.toRoomReleaseGroup
import ly.david.mbjc.data.toUiReleaseGroup
import ly.david.mbjc.ui.common.UiState

@HiltViewModel
class ReleaseGroupsByArtistViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao
) : ViewModel() {

    // TODO: use functional paradigm, don't need to in-memory cache now
    private val musicBrainzReleaseGroups = mutableListOf<MusicBrainzReleaseGroup>()

    val query: MutableStateFlow<String> = MutableStateFlow("")
    val artistId: MutableStateFlow<String> = MutableStateFlow("")

    fun updateArtist(artistId: String) {
        this.artistId.value = artistId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    val uiReleaseGroups: StateFlow<UiState<List<UiReleaseGroup>>> =
        combine(
            artistId.filter { it.isNotEmpty() },
            query
        ) { artistId, query ->
            UiState(
                response = getReleaseGroupsByArtist(artistId, query)
            )
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                UiState(isLoading = true)
            )

    private suspend fun getReleaseGroupsByArtist(
        artistId: String,
        query: String,
        limit: Int = BROWSE_LIMIT,
        offset: Int = 0
    ): List<UiReleaseGroup> {

        // TODO: we could store a count of how many release groups an artist has, so that we can continue fetching if it's not complete
        //  should be in artist's table? Update it there once we get it via browse release groups by artist
        val releaseGroupsByArtistInRoom = releaseGroupDao.getNumberOfReleaseGroupsByArtist(artistId)
        return if (releaseGroupsByArtistInRoom == 0) {
            getReleaseGroupsByArtistFromNetwork(artistId, limit, offset).map { it.toUiReleaseGroup() }
        } else {

            val roomReleaseGroups: List<RoomReleaseGroup> = if (query.isEmpty()) {
                releaseGroupDao.getAllReleaseGroupsByArtist(artistId)
            } else {
                releaseGroupDao.getAllReleaseGroupsByArtistFiltered(artistId, "%$query%")
            }

            roomReleaseGroups.map { it.toUiReleaseGroup(releaseGroupArtistDao.getReleaseGroupArtistCredits(it.id)) }
        }
    }

    private suspend fun getReleaseGroupsByArtistFromNetwork(
        artistId: String,
        limit: Int = BROWSE_LIMIT,
        offset: Int = 0
    ): List<MusicBrainzReleaseGroup> {
        if (offset != 0) {
            delay(DELAY_PAGED_API_CALLS_MS)
        }
        val response = musicBrainzApiService.browseReleaseGroupsByArtist(
            artistId = artistId,
            limit = limit,
            offset = offset
        )

        val newReleaseGroups = response.releaseGroups
        musicBrainzReleaseGroups.addAll(newReleaseGroups)
        return if (musicBrainzReleaseGroups.size < response.releaseGroupCount) {
            getReleaseGroupsByArtistFromNetwork(
                artistId = artistId,
                offset = offset + newReleaseGroups.size
            )
        } else {
            releaseGroupDao.insertAll(musicBrainzReleaseGroups.map { it.toRoomReleaseGroup() })

            // TODO: insert after each api call? That way, if interrupted, we will have saved data
            //  can we also start off where we left off by comparing number of entries vs releaseGroupCount?
            //  If so, provide way to invalidate database and fetch fresh, in case a new release group is added between already fetched data
            releaseGroupArtistDao.insertAll(
                musicBrainzReleaseGroups.flatMap { releaseGroup ->
                    releaseGroup.artistCredits?.mapIndexed { index, artistCredit ->
                        RoomReleaseGroupArtistCredit(
                            releaseGroupId = releaseGroup.id,
                            artistId = artistCredit.artist.id,
                            name = artistCredit.name,
                            joinPhrase = artistCredit.joinPhrase,
                            order = index
                        )
                    }.orEmpty()
                }
            )

            musicBrainzReleaseGroups
        }
    }

}
