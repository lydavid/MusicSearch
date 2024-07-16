package ly.david.musicsearch.data.repository.release

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.insertSeparators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.core.models.getFormatsForDisplay
import ly.david.musicsearch.core.models.getTracksForDisplay
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.TrackListItemModel
import ly.david.musicsearch.core.models.listitem.toAreaListItemModel
import ly.david.musicsearch.core.models.listitem.toLabelListItemModel
import ly.david.musicsearch.core.models.release.ReleaseDetailsModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.release.ReleaseRepository

class ReleaseRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseDao: ReleaseDao,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaDao: AreaDao,
    private val countryCodeDao: CountryCodeDao,
    private val labelDao: LabelDao,
    private val releaseLabelDao: ReleaseLabelDao,
    private val relationRepository: RelationRepository,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
) : ReleaseRepository {

    // TODO: split up what data to include when calling from details/tracks tabs?
    //  initial load only requires 1 api call to display data on both tabs
    //  but swipe to refresh should only refresh its own tab
    override suspend fun lookupRelease(releaseId: String): ReleaseDetailsModel {
        val releaseForDetails = releaseDao.getReleaseForDetails(releaseId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(releaseId)
        val releaseGroup = releaseGroupDao.getReleaseGroupForRelease(releaseId)
        val formatTrackCounts = releaseDao.getReleaseFormatTrackCount(releaseId)
        val labels = releaseLabelDao.getLabelsByRelease(releaseId)
        val releaseEvents = releaseCountryDao.getCountriesByRelease(releaseId)
        val urlRelations = relationRepository.getEntityUrlRelationships(releaseId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(releaseId)
        if (releaseForDetails != null &&
            releaseGroup != null &&
            artistCredits.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits and a release group.
            return releaseForDetails.copy(
                artistCredits = artistCredits,
                releaseGroup = releaseGroup,
                formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
                formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
                labels = labels.map { it.toLabelListItemModel() },
                areas = releaseEvents.map { it.toAreaListItemModel() },
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
                    releaseGroupId = releaseGroup.id,
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
                    it.area
                }.orEmpty(),
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

            val relationWithOrderList = release.relations.toRelationWithOrderList(release.id)
            relationRepository.insertAllUrlRelations(
                entityId = release.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }

    // region tracks by release
    @OptIn(ExperimentalPagingApi::class)
    override fun observeTracksByRelease(
        releaseId: String,
        query: String,
    ): Flow<PagingData<ListItemModel>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = LookupEntityRemoteMediator(
                hasEntityBeenStored = { hasReleaseTracksBeenStored(releaseId) },
                lookupEntity = { lookupRelease(releaseId) },
                deleteLocalEntity = { deleteMediaAndTracksByRelease(releaseId) },
            ),
            pagingSourceFactory = {
                trackDao.getTracksByRelease(
                    releaseId = releaseId,
                    query = "%$query%",
                )
            },
        ).flow.map { pagingData ->
            pagingData
                .insertSeparators { before: TrackListItemModel?, after: TrackListItemModel? ->
                    if (before?.mediumId != after?.mediumId && after != null) {
                        val medium =
                            mediumDao.getMediumForTrack(after.id) ?: return@insertSeparators null

                        ListSeparator(
                            id = "${medium.id}",
                            text = medium.format.orEmpty() +
                                (medium.position?.toString() ?: "").transformThisIfNotNullOrEmpty { " $it" } +
                                medium.name.transformThisIfNotNullOrEmpty { " ($it)" },
                        )
                    } else {
                        null
                    }
                }
        }
    }

    private fun hasReleaseTracksBeenStored(releaseId: String): Boolean {
        // TODO: right now the details tab is coupled with the tracks list tab
        return releaseDao.getReleaseForDetails(releaseId) != null
    }

    private fun deleteMediaAndTracksByRelease(releaseId: String) {
        releaseDao.withTransaction {
            releaseDao.delete(releaseId)
            mediumDao.deleteMediaByRelease(releaseId)
        }
    }
    // endregion
}
