package ly.david.mbjc.ui.release

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.toReleaseUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.getReleaseArtistCreditRoomModels
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.TrackDao
import ly.david.mbjc.data.persistence.release.toMediumRoomModel
import ly.david.mbjc.data.persistence.release.toReleaseRoomModel
import ly.david.mbjc.data.persistence.release.toTrackRoomModel
import ly.david.mbjc.data.persistence.releasegroup.ReleaseReleaseGroup
import ly.david.mbjc.data.persistence.releasegroup.ReleasesReleaseGroupsDao

@Singleton
internal class ReleaseRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    private val releasesReleaseGroupsDao: ReleasesReleaseGroupsDao,
) {

    /**
     * Returns a release for display.
     *
     * Looks up release and stores its data (excludes relationships).
     */
    suspend fun getRelease(releaseId: String): ReleaseUiModel {
        val releaseRoomModel = releaseDao.getRelease(releaseId)
        val artistCredits = releaseDao.getReleaseArtistCredits(releaseId)
        val releaseGroupId = releasesReleaseGroupsDao.getReleaseReleaseGroup(releaseId)
        if (releaseRoomModel != null && artistCredits.isNotEmpty() && releaseGroupId != null) {
            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits.
            return releaseRoomModel.toReleaseUiModel(artistCredits, releaseGroupId)
        }

        // Fetch from network. Store all relevant models.
        val releaseMusicBrainzModel = musicBrainzApiService.lookupRelease(releaseId)
        releaseDao.insert(releaseMusicBrainzModel.toReleaseRoomModel())
        releaseDao.insertAllArtistCredits(releaseMusicBrainzModel.getReleaseArtistCreditRoomModels())
        releaseMusicBrainzModel.releaseGroup?.let { releaseGroup ->
            releasesReleaseGroupsDao.insert(
                ReleaseReleaseGroup(
                    releaseId = releaseId,
                    releaseGroupId = releaseGroup.id
                )
            )
        }

        releaseMusicBrainzModel.media?.forEach { medium ->
            val mediumId = mediumDao.insert(medium.toMediumRoomModel(releaseMusicBrainzModel.id))
            trackDao.insertAll(medium.tracks?.map { it.toTrackRoomModel(mediumId) } ?: emptyList())
        }
        return releaseMusicBrainzModel.toReleaseUiModel()
    }
}
