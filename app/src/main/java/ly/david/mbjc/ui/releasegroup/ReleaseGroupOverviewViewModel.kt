package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.UiReleaseGroup
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupDao
import ly.david.mbjc.data.persistence.toRoomReleaseGroup
import ly.david.mbjc.data.toUiReleaseGroup
import ly.david.mbjc.ui.Destination

// TODO: Can Dropbox Store simplify this?
@Singleton
class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var releaseGroup: UiReleaseGroup? = null

    suspend fun lookupReleaseGroup(releaseGroupId: String): UiReleaseGroup =
        releaseGroup ?: run {

            val roomReleaseGroup = releaseGroupDao.getReleaseGroup(releaseGroupId)
            if (roomReleaseGroup != null) {
                incrementOrInsertLookupHistory(roomReleaseGroup)
                return roomReleaseGroup.toUiReleaseGroup(releaseGroupArtistDao.getReleaseGroupArtistCredits(releaseGroupId))
            }

            val musicBrainzReleaseGroup = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)
            releaseGroupDao.insert(musicBrainzReleaseGroup.toRoomReleaseGroup())
            incrementOrInsertLookupHistory(musicBrainzReleaseGroup)
            musicBrainzReleaseGroup.toUiReleaseGroup()
        }

    private suspend fun incrementOrInsertLookupHistory(releaseGroup: ReleaseGroup) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = releaseGroup.title,
                destination = Destination.LOOKUP_RELEASE_GROUP,
                mbid = releaseGroup.id
            )
        )
    }
}

@HiltViewModel
class ReleaseGroupOverviewViewModel @Inject constructor(
    private val releaseGroupRepository: ReleaseGroupRepository
) : ViewModel() {

    suspend fun lookupReleaseGroup(releaseGroupId: String): UiReleaseGroup =
        releaseGroupRepository.lookupReleaseGroup(releaseGroupId)
}
