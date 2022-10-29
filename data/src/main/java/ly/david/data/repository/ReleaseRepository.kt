package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.AreaType
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.toReleaseUiModel
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.getReleaseArtistCreditRoomModels
import ly.david.data.network.getReleaseGroupArtistCreditRoomModels
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.area.getAreaCountryCodes
import ly.david.data.persistence.area.getReleaseCountries
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.artist.ReleaseGroupArtistDao
import ly.david.data.persistence.release.MediumDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.TrackDao
import ly.david.data.persistence.release.toMediumRoomModel
import ly.david.data.persistence.release.toReleaseRoomModel
import ly.david.data.persistence.release.toTrackRoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.releasegroup.toReleaseGroupRoomModel

@Singleton
class ReleaseRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
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

        // Empty artist credits is sufficient to indicate we've never done a lookup
        // if it's no longer the case, then check for formats/tracks
        if (releaseRoomModel != null && artistCredits.isNotEmpty()) {
            val releaseGroup = releaseGroupDao.getReleaseGroup(releaseRoomModel.releaseGroupId)

            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits.
            return releaseRoomModel.toReleaseUiModel(artistCredits, releaseGroup?.id)
        }

        // Fetch from network. Store all relevant models.
        val releaseMusicBrainzModel = musicBrainzApiService.lookupRelease(releaseId)

        // Insert RG first due to FK constraint
        releaseMusicBrainzModel.releaseGroup?.let {
            releaseGroupDao.insert(it.toReleaseGroupRoomModel())
            releaseGroupArtistDao.insertAll(it.getReleaseGroupArtistCreditRoomModels())
        }

        releaseDao.insertReplace(releaseMusicBrainzModel.toReleaseRoomModel())

        releaseDao.insertAllArtistCredits(releaseMusicBrainzModel.getReleaseArtistCreditRoomModels())

        // TODO: labels

        releaseMusicBrainzModel.media?.forEach { medium ->
            val mediumId = mediumDao.insert(medium.toMediumRoomModel(releaseMusicBrainzModel.id))
            trackDao.insertAll(medium.tracks?.map { it.toTrackRoomModel(mediumId) } ?: emptyList())
        }

        // Note that if you visit a release's screen before its country's releases page,
        // you will most likely see this release on top (at least until the rest of the releases
        // are loaded, then it should be sorted by whatever method we chose.
        // We will consider this the expected behaviour.
        releasesCountriesDao.insertAll(releaseMusicBrainzModel.getReleaseCountries())

        areaDao.insertAll(
            releaseMusicBrainzModel.releaseEvents?.mapNotNull {
                // release events returns null type, but we know they are countries
                // Except in the case of [Worldwide], but it will replace itself when we first visit it.
                it.area?.toAreaRoomModel()?.copy(type = AreaType.COUNTRY)
            }.orEmpty()
        )
        areaDao.insertAllCountryCodes(
            releaseMusicBrainzModel.releaseEvents?.flatMap { releaseEvent ->
                releaseEvent.area?.getAreaCountryCodes().orEmpty()
            }.orEmpty()
        )

        return releaseMusicBrainzModel.toReleaseUiModel()
    }
}
