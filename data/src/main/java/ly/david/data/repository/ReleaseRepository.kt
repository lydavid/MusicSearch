package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.AreaType
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.toReleaseUiModel
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.getReleaseArtistCreditRoomModels
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.area.getAreaCountryCodes
import ly.david.data.persistence.area.getReleaseCountries
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.release.MediumDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.TrackDao
import ly.david.data.persistence.release.toMediumRoomModel
import ly.david.data.persistence.release.toReleaseRoomModel
import ly.david.data.persistence.release.toTrackRoomModel
import ly.david.data.persistence.releasegroup.ReleaseReleaseGroup
import ly.david.data.persistence.releasegroup.ReleasesReleaseGroupsDao

@Singleton
class ReleaseRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    private val releasesReleaseGroupsDao: ReleasesReleaseGroupsDao,
    private val releasesCountriesDao: ReleasesCountriesDao,
    private val areaDao: AreaDao
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

        // TODO: this will cause this release to appear on top of Area's Releases tab
        //  since it's inserting before the rest
        releasesCountriesDao.insertAll(releaseMusicBrainzModel.getReleaseCountries())

        areaDao.insertAll(
            releaseMusicBrainzModel.releaseEvents?.mapNotNull {
                // release events returns null type, but we know they are countries
                it.area?.toAreaRoomModel()?.copy(type = AreaType.COUNTRY)
            }.orEmpty()
        )

        areaDao.insertAllCountryCodes(
            releaseMusicBrainzModel.releaseEvents?.flatMap { releaseEvent ->
                releaseEvent.area?.getAreaCountryCodes().orEmpty()
            }.orEmpty()
        )

        // TODO: create the area as well, which only needs id/name/type (always country)
        //  when we navigate to area for the first time via here, how can we make sure to still fetch relations?

        return releaseMusicBrainzModel.toReleaseUiModel()
    }
}
