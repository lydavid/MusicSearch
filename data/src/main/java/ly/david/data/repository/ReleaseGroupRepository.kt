package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.ReleaseGroupUiModel
import ly.david.data.domain.toReleaseGroupUiModel
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.getReleaseGroupArtistCreditRoomModels
import ly.david.data.persistence.artist.ReleaseGroupArtistDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.releasegroup.toReleaseGroupRoomModel

@Singleton
class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao,
) {
    private var releaseGroup: ReleaseGroupUiModel? = null

    // We need ReleaseGroupUiModel so that we have artist credits
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupUiModel =
        releaseGroup ?: run {

            val roomReleaseGroup = releaseGroupDao.getReleaseGroup(releaseGroupId)
            if (roomReleaseGroup != null) {
                return roomReleaseGroup.toReleaseGroupUiModel(
                    releaseGroupArtistDao.getReleaseGroupArtistCredits(
                        releaseGroupId
                    )
                )
            }

            val musicBrainzReleaseGroup = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)

            // Whenever we insert a release group, we must always insert all of its artists as well
            releaseGroupDao.insert(musicBrainzReleaseGroup.toReleaseGroupRoomModel())
            releaseGroupArtistDao.insertAll(musicBrainzReleaseGroup.getReleaseGroupArtistCreditRoomModels())

            musicBrainzReleaseGroup.toReleaseGroupUiModel()
        }.also { releaseGroup = it }
}
