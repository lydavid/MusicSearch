package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.AreaType
import ly.david.data.domain.ReleaseScaffoldModel
import ly.david.data.domain.toScaffoldModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.toLabelRoomModels
import ly.david.data.network.toReleaseLabels
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.area.getAreaCountryCodes
import ly.david.data.persistence.area.getReleaseCountries
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.label.LabelDao
import ly.david.data.persistence.label.ReleasesLabelsDao
import ly.david.data.persistence.release.MediumDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.TrackDao
import ly.david.data.persistence.release.toMediumRoomModel
import ly.david.data.persistence.release.toTrackRoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupDao

@Singleton
class ReleaseRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    private val releasesCountriesDao: ReleasesCountriesDao,
    private val areaDao: AreaDao,
    private val labelDao: LabelDao,
    private val releasesLabelsDao: ReleasesLabelsDao
): RelationsListRepository {

    /**
     * Returns a release for display.
     *
     * Looks up release and stores its data (excludes relationships).
     */
    suspend fun getRelease(releaseId: String): ReleaseScaffoldModel {
        val releaseWithAllData = releaseDao.getReleaseWithAllData(releaseId)

        if (releaseWithAllData != null &&
            releaseWithAllData.artistCreditNamesWithResources.isNotEmpty() &&
            releaseWithAllData.releaseGroup != null
        ) {
            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits and a release group.
            return releaseWithAllData.toScaffoldModel()
        }

        // Fetch from network. Store all relevant models.
        val releaseMusicBrainzModel = musicBrainzApiService.lookupRelease(releaseId)

        releaseMusicBrainzModel.releaseGroup?.let {
            releaseGroupDao.insertReleaseGroupWithArtistCredits(it)
        }

        releaseDao.insertReleaseWithArtistCredits(releaseMusicBrainzModel)

        // TODO: transaction
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

        return getRelease(releaseId)
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupRelease(
            releaseId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
