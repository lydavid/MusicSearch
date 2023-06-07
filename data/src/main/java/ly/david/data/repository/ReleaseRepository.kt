package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.AreaType
import ly.david.data.domain.ReleaseScaffoldModel
import ly.david.data.domain.toReleaseScaffoldModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.area.AreaDao
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.area.getAreaCountryCodes
import ly.david.data.room.area.releases.getReleaseCountries
import ly.david.data.room.area.toAreaRoomModel
import ly.david.data.room.label.LabelDao
import ly.david.data.room.label.releases.ReleaseLabelDao
import ly.david.data.room.label.releases.toReleaseLabels
import ly.david.data.room.label.toRoomModels
import ly.david.data.room.release.tracks.MediumDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.tracks.TrackDao
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroup
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroupDao
import ly.david.data.room.release.tracks.toMediumRoomModel
import ly.david.data.room.releasegroup.ReleaseGroupDao

// TODO: move back to ui folder
@Singleton
class ReleaseRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaDao: AreaDao,
    private val labelDao: LabelDao,
    private val releaseLabelDao: ReleaseLabelDao
) : RelationsListRepository {

    // TODO: split up what data to include when calling from details/tracks tabs?
    //  initial load only requires 1 api call to display data on both tabs
    //  but swipe to refresh should only refresh its own tab
    /**
     * Returns a release for display.
     *
     * Looks up release and stores its data (excludes relationships).
     */
    suspend fun lookupRelease(releaseId: String): ReleaseScaffoldModel {
        val releaseWithAllData = releaseDao.getReleaseWithAllData(releaseId)

        if (releaseWithAllData != null &&
            releaseWithAllData.artistCreditNamesWithResources.isNotEmpty() &&
            releaseWithAllData.formatTrackCounts.isNotEmpty()
        ) {
            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits and a release group.
            return releaseWithAllData.toReleaseScaffoldModel()
        }

        // Fetch from network. Store all relevant models.
        val releaseMusicBrainzModel = musicBrainzApiService.lookupRelease(releaseId)
        insertAllModels(releaseMusicBrainzModel)
        return lookupRelease(releaseId)
    }

    private suspend fun insertAllModels(release: ReleaseMusicBrainzModel) {
        releaseDao.withTransaction {
            release.releaseGroup?.let { releaseGroup ->
                releaseGroupDao.insertReleaseGroupWithArtistCredits(releaseGroup)
                releaseReleaseGroupDao.insert(
                    ReleaseReleaseGroup(
                        releaseId = release.id,
                        releaseGroupId = releaseGroup.id
                    )
                )
            }
            releaseDao.insertReleaseWithArtistCredits(release)

            // This serves as a replacement for browsing labels by release.
            // Unless we find a release that has more than 25 labels, we don't need to browse for labels.
            labelDao.insertAll(release.labelInfoList?.toRoomModels().orEmpty())
            releaseLabelDao.insertAll(
                release.labelInfoList?.toReleaseLabels(releaseId = release.id).orEmpty()
            )

            release.media?.forEach { medium ->
                val mediumId = mediumDao.insert(medium.toMediumRoomModel(release.id))
                medium.tracks?.forEach { track ->
                    trackDao.insertTrackWithArtistCredits(track, mediumId)
                }
            }

            areaDao.insertAll(
                release.releaseEvents?.mapNotNull {
                    // release events returns null type, but we know they are countries
                    // Except in the case of [Worldwide], but it will replace itself when we first visit it.
                    it.area?.toAreaRoomModel()?.copy(type = AreaType.COUNTRY)
                }.orEmpty()
            )
            areaDao.insertAllCountryCodes(
                release.releaseEvents?.flatMap { releaseEvent ->
                    releaseEvent.area?.getAreaCountryCodes().orEmpty()
                }.orEmpty()
            )
            releaseCountryDao.insertAll(release.getReleaseCountries())
        }
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupRelease(
            releaseId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
