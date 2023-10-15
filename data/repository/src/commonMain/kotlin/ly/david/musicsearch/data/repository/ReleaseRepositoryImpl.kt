package ly.david.musicsearch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.insertSeparators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.area.AreaType
import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.core.models.getFormatsForDisplay
import ly.david.musicsearch.core.models.getTracksForDisplay
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.listitem.TrackListItemModel
import ly.david.musicsearch.core.models.listitem.toAreaListItemModel
import ly.david.musicsearch.core.models.listitem.toLabelListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.release.ReleaseScaffoldModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.relation.RelationRepository
import ly.david.musicsearch.domain.release.ReleaseRepository
import lydavidmusicsearchdatadatabase.Browse_entity_count

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
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val recordingReleaseDao: RecordingReleaseDao,
    private val artistReleaseDao: ArtistReleaseDao,
) : ReleaseRepository {

    // TODO: split up what data to include when calling from details/tracks tabs?
    //  initial load only requires 1 api call to display data on both tabs
    //  but swipe to refresh should only refresh its own tab
    override suspend fun lookupRelease(releaseId: String): ReleaseScaffoldModel {
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
                    // release events returns null type, but we know they are countries
                    // Except in the case of [Worldwide], but it will replace itself when we first visit it.
                    it.area?.copy(type = AreaType.COUNTRY)
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

    // region releases by entity
    @OptIn(ExperimentalPagingApi::class)
    override fun observeReleasesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        query: String,
        isRemote: Boolean,
    ): Flow<PagingData<ReleaseListItemModel>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = getRemoteMediator(
                entityId = entityId,
                entity = entity,
            ).takeIf { isRemote },
            pagingSourceFactory = {
                getLinkedEntitiesPagingSource(
                    entityId = entityId,
                    entity = entity,
                    query = query,
                )
            },
        ).flow
    }

    private fun getRemoteMediator(
        entityId: String,
        entity: MusicBrainzEntity,
    ) = BrowseEntityRemoteMediator<ReleaseListItemModel>(
        getRemoteEntityCount = { getRemoteLinkedEntitiesCountByEntity(entityId) },
        getLocalEntityCount = { getLocalLinkedEntitiesCountByEntity(entityId) },
        deleteLocalEntity = {
            deleteLinkedEntitiesByEntity(
                entityId = entityId,
                entity = entity,
            )
        },
        browseEntity = { offset ->
            browseLinkedEntitiesAndStore(
                entityId = entityId,
                entity = entity,
                nextOffset = offset,
            )
        },
    )

    private fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        browseEntityCountDao.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = MusicBrainzEntity.RELEASE,
        )?.remoteCount

    private fun getLocalLinkedEntitiesCountByEntity(entityId: String) =
        browseEntityCountDao.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = MusicBrainzEntity.RELEASE,
        )?.localCount ?: 0

    private fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        when (entity) {
            MusicBrainzEntity.AREA -> {
                releaseCountryDao.withTransaction {
                    releaseCountryDao.deleteReleasesByCountry(entityId)
                    browseEntityCountDao.deleteBrowseEntityCountByEntity(
                        entityId = entityId,
                        browseEntity = MusicBrainzEntity.RELEASE,
                    )
                }
            }

            MusicBrainzEntity.ARTIST -> {
                artistReleaseDao.withTransaction {
                    artistReleaseDao.deleteReleasesByArtist(entityId)
                    browseEntityCountDao.deleteBrowseEntityCountByEntity(
                        entityId,
                        MusicBrainzEntity.RELEASE,
                    )
                }
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.withTransaction {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                    browseEntityCountDao.deleteBrowseEntityCountByEntity(
                        entityId,
                        MusicBrainzEntity.RELEASE,
                    )
                }
            }

            MusicBrainzEntity.LABEL -> {
                releaseLabelDao.withTransaction {
                    releaseLabelDao.deleteReleasesByLabel(entityId)
                    browseEntityCountDao.deleteBrowseEntityCountByEntity(
                        entityId,
                        MusicBrainzEntity.RELEASE,
                    )
                }
            }

            MusicBrainzEntity.RECORDING -> {
                recordingReleaseDao.withTransaction {
                    recordingReleaseDao.deleteReleasesByRecording(entityId)
                    browseEntityCountDao.deleteBrowseEntityCountByEntity(
                        entityId,
                        MusicBrainzEntity.RELEASE,
                    )
                }
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                releaseReleaseGroupDao.withTransaction {
                    releaseReleaseGroupDao.deleteReleasesByReleaseGroup(entityId)
                    browseEntityCountDao.deleteBrowseEntityCountByEntity(
                        entityId,
                        MusicBrainzEntity.RELEASE,
                    )
                }
            }

            else -> {}
        }
    }

    private fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        query: String,
    ): PagingSource<Int, ReleaseListItemModel> {
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                releaseCountryDao.getReleasesByCountry(
                    areaId = entityId,
                    query = "%$query%",
                )
            }

            MusicBrainzEntity.ARTIST -> {
                artistReleaseDao.getReleasesByArtist(
                    artistId = entityId,
                    query = "%$query%",
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getReleasesByCollection(
                    collectionId = entityId,
                    query = "%$query%",
                )
            }

            MusicBrainzEntity.LABEL -> {
                releaseLabelDao.getReleasesByLabel(
                    labelId = entityId,
                    query = "%$query%",
                )
            }

            MusicBrainzEntity.RECORDING -> {
                recordingReleaseDao.getReleasesByRecording(
                    recordingId = entityId,
                    query = "%$query%",
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                releaseReleaseGroupDao.getReleasesByReleaseGroup(
                    releaseGroupId = entityId,
                    query = "%$query%",
                )
            }

            else -> error("Releases by $entity not supported.")
        }
    }

    private suspend fun browseLinkedEntitiesAndStore(
        entityId: String,
        entity: MusicBrainzEntity,
        nextOffset: Int,
    ): Int {
        val response = browseReleasesByEntity(
            entityId = entityId,
            entity = entity,
            offset = nextOffset,
        )

        if (response.offset == 0) {
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = MusicBrainzEntity.RELEASE,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count,
                ),
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.RELEASE,
                additionalOffset = response.musicBrainzModels.size,
            )
        }

        val releaseMusicBrainzModels = response.musicBrainzModels
        releaseDao.insertAll(releaseMusicBrainzModels)
        insertAllLinkingModels(
            entityId = entityId,
            entity = entity,
            releaseMusicBrainzModels = releaseMusicBrainzModels,
        )

        return releaseMusicBrainzModels.size
    }

    private suspend fun browseReleasesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseReleasesResponse {
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                musicBrainzApi.browseReleasesByArea(
                    areaId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.ARTIST -> {
                musicBrainzApi.browseReleasesByArtist(
                    artistId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                musicBrainzApi.browseReleasesByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.LABEL -> {
                musicBrainzApi.browseReleasesByLabel(
                    labelId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                musicBrainzApi.browseReleasesByRecording(
                    recordingId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                musicBrainzApi.browseReleasesByReleaseGroup(
                    releaseGroupId = entityId,
                    offset = offset,
                )
            }

            else -> error("Releases by $entity not supported.")
        }
    }

    private fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        when (entity) {
            MusicBrainzEntity.AREA -> {
                releaseCountryDao.linkReleasesByCountry(
                    areaId = entityId,
                    releases = releaseMusicBrainzModels,
                )
            }

            MusicBrainzEntity.ARTIST -> {
                artistReleaseDao.insertAll(
                    artistId = entityId,
                    releaseIds = releaseMusicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = releaseMusicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntity.LABEL -> {
                releaseLabelDao.linkReleasesByLabel(
                    labelId = entityId,
                    releases = releaseMusicBrainzModels,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                recordingReleaseDao.insertAll(
                    recordingId = entityId,
                    releaseIds = releaseMusicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                releaseReleaseGroupDao.insertAll(
                    releaseGroupId = entityId,
                    releaseIds = releaseMusicBrainzModels.map { release -> release.id },
                )
            }

            else -> {}
        }
    }
    // endregion
}
