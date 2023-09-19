package ly.david.data.domain.release

import ly.david.data.core.AreaType
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.area.RoomAreaDao
import ly.david.data.room.area.getAreaCountryCodes
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.area.releases.getReleaseCountries
import ly.david.data.room.area.toAreaRoomModel
import ly.david.data.room.label.RoomLabelDao
import ly.david.data.room.label.releases.ReleaseLabelDao
import ly.david.data.room.label.releases.toReleaseLabels
import ly.david.data.room.label.toRoomModels
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.tracks.MediumDao
import ly.david.data.room.release.tracks.TrackDao
import ly.david.data.room.release.tracks.toMediumRoomModel
import ly.david.data.room.releasegroup.RoomReleaseGroupDao
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroup
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroupDao
import org.koin.core.annotation.Single

@Single
class ReleaseRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseDao: ReleaseDao,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    private val releaseGroupDao: RoomReleaseGroupDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaDao: RoomAreaDao,
    private val labelDao: RoomLabelDao,
    private val releaseLabelDao: ReleaseLabelDao,
    private val relationRepository: RelationRepository,
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
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(releaseId)
        if (releaseWithAllData != null &&
            releaseWithAllData.artistCreditNamesWithEntities.isNotEmpty() &&
            releaseWithAllData.formatTrackCounts.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits and a release group.
            return releaseWithAllData.toReleaseScaffoldModel()
        }

        // Fetch from network. Store all relevant models.
        val releaseMusicBrainzModel = musicBrainzApi.lookupRelease(releaseId)
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

            relationRepository.insertAllUrlRelations(
                entityId = release.id,
                relationMusicBrainzModels = release.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupRelease(
            releaseId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
