package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.AreaType
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.toReleaseGroupUiModel
import ly.david.data.domain.toReleaseUiModel
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.getReleaseArtistCreditRoomModels
import ly.david.data.network.getReleaseGroupArtistCreditRoomModels
import ly.david.data.network.toLabelRoomModels
import ly.david.data.network.toReleaseLabels
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.area.getAreaCountryCodes
import ly.david.data.persistence.area.getReleaseCountries
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.artist.ReleaseGroupArtistDao
import ly.david.data.persistence.label.LabelDao
import ly.david.data.persistence.label.ReleasesLabelsDao
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
    private val areaDao: AreaDao,
    private val labelDao: LabelDao,
    private val releasesLabelsDao: ReleasesLabelsDao
) {

    /**
     * Returns a release for display.
     *
     * Looks up release and stores its data (excludes relationships).
     */
    suspend fun getRelease(releaseId: String): ReleaseUiModel {
        // TODO: could we use @Relation even if some of these are not FK?
        val releaseWithAllData = releaseDao.getReleaseWithAllData(releaseId)
        val artistCredits = releaseDao.getReleaseArtistCredits(releaseId)

        // Empty artist credits is sufficient to indicate we've never done a lookup
        // if it's no longer the case, then check for formats/tracks
        if (releaseWithAllData != null && artistCredits.isNotEmpty() && releaseWithAllData.release.releaseGroupId != null) {
            val releaseGroup = releaseGroupDao.getReleaseGroup(releaseWithAllData.release.releaseGroupId)

            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits.
            return releaseWithAllData.toReleaseUiModel(artistCredits, releaseGroup?.toReleaseGroupUiModel())
        }

        // Fetch from network. Store all relevant models.
        val releaseMusicBrainzModel = musicBrainzApiService.lookupRelease(releaseId)

        releaseMusicBrainzModel.releaseGroup?.let {
            releaseGroupDao.insert(it.toReleaseGroupRoomModel())
            releaseGroupArtistDao.insertAll(it.getReleaseGroupArtistCreditRoomModels())
        }

        releaseDao.insertReplace(releaseMusicBrainzModel.toReleaseRoomModel())
        releaseDao.insertAllArtistCredits(releaseMusicBrainzModel.getReleaseArtistCreditRoomModels())

        labelDao.insertAll(releaseMusicBrainzModel.labelInfoList?.toLabelRoomModels().orEmpty())
        releasesLabelsDao.insertAll(
            releaseMusicBrainzModel.labelInfoList?.toReleaseLabels(releaseId = releaseId).orEmpty()
        )

        releaseMusicBrainzModel.media?.forEach { medium ->
            val mediumId = mediumDao.insert(medium.toMediumRoomModel(releaseMusicBrainzModel.id))
            trackDao.insertAll(medium.tracks?.map { it.toTrackRoomModel(mediumId) } ?: emptyList())
        }

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
