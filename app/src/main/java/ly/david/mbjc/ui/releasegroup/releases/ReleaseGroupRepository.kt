package ly.david.mbjc.ui.releasegroup.releases

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.domain.ReleaseGroupUiModel
import ly.david.mbjc.data.domain.toReleaseGroupUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.getRoomReleaseGroupArtistCredit
import ly.david.mbjc.data.persistence.artist.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.mbjc.data.persistence.releasegroup.toReleaseGroupRoomModel

@Singleton
internal class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var releaseGroup: ReleaseGroupUiModel? = null

    // We need ReleaseGroupUiModel so that we have artist credits
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupUiModel =
        releaseGroup ?: run {

            val roomReleaseGroup = releaseGroupDao.getReleaseGroup(releaseGroupId)
            if (roomReleaseGroup != null) {
                incrementOrInsertLookupHistory(roomReleaseGroup)
                return roomReleaseGroup.toReleaseGroupUiModel(
                    releaseGroupArtistDao.getReleaseGroupArtistCredits(
                        releaseGroupId
                    )
                )
            }

            val musicBrainzReleaseGroup = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)

            // Whenever we insert a release group, we must always insert all of its artists as well
            releaseGroupDao.insert(musicBrainzReleaseGroup.toReleaseGroupRoomModel())
            releaseGroupArtistDao.insertAll(musicBrainzReleaseGroup.getRoomReleaseGroupArtistCredit())

            incrementOrInsertLookupHistory(musicBrainzReleaseGroup)

            musicBrainzReleaseGroup.toReleaseGroupUiModel()
        }

    private suspend fun incrementOrInsertLookupHistory(releaseGroup: ReleaseGroup) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = releaseGroup.name,
                resource = MusicBrainzResource.RELEASE_GROUP,
                mbid = releaseGroup.id
            )
        )
    }
}
