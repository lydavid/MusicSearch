package ly.david.data.domain.release

import ly.david.data.core.area.AreaType
import ly.david.data.core.image.ImageUrlDao
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import org.koin.core.annotation.Single

@Single
class ReleaseRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseDao: ReleaseDao,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val imageUrlDao: ImageUrlDao,
    private val artistCreditDao: ArtistCreditDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaDao: AreaDao,
    private val countryCodeDao: CountryCodeDao,
    private val labelDao: LabelDao,
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
        val release = releaseDao.getRelease(releaseId)
        val artistCreditNames = artistCreditDao.getArtistCreditNamesForEntity(releaseId)
        val releaseGroup = releaseGroupDao.getReleaseGroupForRelease(releaseId)
        val largeImageUrl = imageUrlDao.getLargeUrlForEntity(releaseId)
        val formatTrackCounts = releaseDao.getReleaseFormatTrackCount(releaseId)
        val labels = releaseLabelDao.getLabelsByRelease(releaseId)
        val releaseEvents = releaseCountryDao.getCountriesByRelease(releaseId)
        val urlRelations = relationRepository.getEntityUrlRelationships(releaseId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(releaseId)
        if (release != null &&
            releaseGroup != null &&
            artistCreditNames.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits and a release group.
            return release.toReleaseScaffoldModel(
                artistCreditNames = artistCreditNames,
                releaseGroup = releaseGroup,
                imageUrl = largeImageUrl,
                formatTrackCounts = formatTrackCounts,
                labels = labels,
                releaseEvents = releaseEvents,
                urls = urlRelations,
            )
        }

        val releaseMusicBrainzModel = musicBrainzApi.lookupRelease(releaseId)
        cache(releaseMusicBrainzModel)
        return lookupRelease(releaseId)
    }

    private fun cache(release: ReleaseMusicBrainzModel) {
        releaseDao.withTransaction {
            release.releaseGroup?.let { releaseGroup ->
                releaseGroupDao.insert(releaseGroup)
                releaseReleaseGroupDao.insert(
                    releaseId = release.id,
                    releaseGroupId = releaseGroup.id
                )
            }
            releaseDao.insert(release)

            // This serves as a replacement for browsing labels by release.
            // Unless we find a release that has more than 25 labels, we don't need to browse for labels.
            labelDao.insertAll(release.labelInfoList?.mapNotNull { it.label })
            releaseLabelDao.linkLabelsByRelease(
                releaseId = release.id,
                labelInfoList = release.labelInfoList,
            )

            areaDao.insertAll(
                release.releaseEvents?.mapNotNull {
                    // release events returns null type, but we know they are countries
                    // Except in the case of [Worldwide], but it will replace itself when we first visit it.
                    it.area?.copy(type = AreaType.COUNTRY)
                }.orEmpty()
            )
            release.releaseEvents?.forEach {
                countryCodeDao.insertCountryCodesForArea(
                    areaId = it.area?.id ?: return@forEach,
                    countryCodes = it.area?.countryCodes,
                )
            }
            releaseCountryDao.linkCountriesByRelease(
                releaseId = release.id,
                releaseEvents = release.releaseEvents,
            )

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
