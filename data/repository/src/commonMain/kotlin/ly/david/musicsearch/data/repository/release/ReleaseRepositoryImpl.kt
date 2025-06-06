package ly.david.musicsearch.data.repository.release

import androidx.paging.Pager
import androidx.paging.TerminalSeparatorType
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.PagingData
import app.cash.paging.insertSeparators
import app.cash.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackAndMedium
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.area.AreaType
import ly.david.musicsearch.shared.domain.area.NonCountryAreaWithCode
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.getFormatsForDisplay
import ly.david.musicsearch.shared.domain.getTracksForDisplay
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.shared.domain.listitem.toAreaListItemModel
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.release.ReleaseRepository

class ReleaseRepositoryImpl(
    private val releaseDao: ReleaseDao,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val areaDao: AreaDao,
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    private val lookupApi: LookupApi,
) : ReleaseRepository {

    // TODO: split up what data to include when calling from details/tracks tabs?
    //  initial load only requires 1 api call to display data on both tabs
    //  but swipe to refresh should only refresh its own tab
    //  I'm leaning towards not doing this to keep things simple
    override suspend fun lookupRelease(
        releaseId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ReleaseDetailsModel {
        if (forceRefresh) {
            delete(releaseId)
        }

        val releaseDetailsModel = releaseDao.getReleaseForDetails(releaseId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(releaseId)
        val releaseGroup = releaseGroupDao.getReleaseGroupForRelease(releaseId)
        val formatTrackCounts = releaseDao.getReleaseFormatTrackCount(releaseId)
        val labels = labelDao.getLabelsByRelease(releaseId)
        val releaseEvents = areaDao.getCountriesByRelease(releaseId)
        val urlRelations = relationRepository.getRelationshipsByType(releaseId)
        val visited = relationRepository.visited(releaseId)

        if (
            releaseDetailsModel != null &&
            releaseGroup != null &&
            artistCredits.isNotEmpty() &&
            visited &&
            !forceRefresh
        ) {
            // According to MB database schema: https://musicbrainz.org/doc/MusicBrainz_Database/Schema
            // releases must have artist credits and a release group.
            return releaseDetailsModel.copy(
                artistCredits = artistCredits,
                releaseGroup = releaseGroup,
                formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
                formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
                labels = labels,
                areas = releaseEvents.map { it.toAreaListItemModel() },
                urls = urlRelations,
            )
        }

        val releaseMusicBrainzModel = lookupApi.lookupRelease(releaseId)
        cache(
            release = releaseMusicBrainzModel,
            lastUpdated = lastUpdated,
        )
        return lookupRelease(
            releaseId = releaseId,
            forceRefresh = false,
            lastUpdated = lastUpdated,
        )
    }

    private fun delete(releaseId: String) {
        releaseDao.withTransaction {
            releaseDao.delete(releaseId = releaseId)
            releaseReleaseGroupDao.deleteReleaseGroupByReleaseLink(releaseId = releaseId)
            labelDao.deleteReleaseLabelLinks(releaseId = releaseId)
            areaDao.deleteCountriesByReleaseLinks(releaseId = releaseId)
            relationRepository.deleteRelationshipsByType(entityId = releaseId)
            artistCreditDao.deleteArtistCreditsForEntity(entityId = releaseId)
        }
    }

    private fun cache(
        release: ReleaseMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        releaseDao.withTransaction {
            release.releaseGroup?.let { releaseGroup ->
                releaseGroupDao.insertReleaseGroup(releaseGroup)
                releaseReleaseGroupDao.insert(
                    releaseId = release.id,
                    releaseGroupId = releaseGroup.id,
                )
            }
            releaseDao.insert(release)

            // This serves as a replacement for browsing labels by release.
            // Unless we find a release that has more than 25 labels, we don't need to browse for labels.
            labelDao.insertAll(release.labelInfoList?.mapNotNull { it.label })
            labelDao.insertLabelsByRelease(
                releaseId = release.id,
                labelInfoList = release.labelInfoList,
            )

            areaDao.insertAll(
                release.releaseEvents?.mapNotNull { event ->
                    event.area?.let { area ->
                        val isCountry = !NonCountryAreaWithCode.entries.any {
                            it.code == area.countryCodes?.firstOrNull().orEmpty()
                        }

                        area.copy(
                            type = if (isCountry) AreaType.COUNTRY else null,
                        )
                    }
                }.orEmpty(),
            )
            areaDao.linkCountriesByRelease(
                releaseId = release.id,
                releaseEvents = release.releaseEvents,
            )

            val relationWithOrderList = release.relations.toRelationWithOrderList(release.id)
            relationRepository.insertAllUrlRelations(
                entityId = release.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }

    // region tracks by release
    @OptIn(ExperimentalPagingApi::class)
    override fun observeTracksByRelease(
        releaseId: String,
        query: String,
        lastUpdated: Instant,
    ): Flow<PagingData<ListItemModel>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = LookupEntityRemoteMediator(
                hasEntityBeenStored = { hasReleaseTracksBeenStored(releaseId) },
                lookupEntity = {
                    lookupRelease(
                        releaseId = releaseId,
                        forceRefresh = false,
                        lastUpdated = lastUpdated,
                    )
                },
                deleteLocalEntity = { deleteMediaAndTracksByRelease(releaseId) },
            ),
            pagingSourceFactory = {
                trackDao.getTracksByRelease(
                    releaseId = releaseId,
                    query = "%$query%",
                )
            },
        ).flow
            .map { pagingData ->
                pagingData
                    .map { it.toTrackListItemModel() }
                    .insertSeparators(
                        terminalSeparatorType = TerminalSeparatorType.SOURCE_COMPLETE,
                    ) { before: TrackListItemModel?, after: TrackListItemModel? ->
                        if (before?.mediumId != after?.mediumId && after != null) {
                            ListSeparator(
                                id = "${after.mediumId}",
                                text = after.format.orEmpty() +
                                    (after.mediumPosition?.toString() ?: "").transformThisIfNotNullOrEmpty { " $it" } +
                                    after.mediumName.transformThisIfNotNullOrEmpty { " ($it)" },
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
            mediumDao.deleteMediaByRelease(releaseId)
            releaseDao.delete(releaseId)
        }
    }
    // endregion
}

private fun TrackAndMedium.toTrackListItemModel() =
    TrackListItemModel(
        id = id,
        position = position,
        number = number,
        title = title,
        length = length,
        mediumId = mediumId,
        recordingId = recordingId,
        formattedArtistCredits = formattedArtistCredits,
        visited = visited,
        mediumPosition = mediumPosition,
        mediumName = mediumName,
        trackCount = trackCount,
        format = format,
    )
