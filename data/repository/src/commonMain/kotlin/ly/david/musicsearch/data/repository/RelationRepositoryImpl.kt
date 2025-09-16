package ly.david.musicsearch.data.repository

import androidx.paging.Pager
import androidx.paging.TerminalSeparatorType
import androidx.paging.map
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.PagingData
import app.cash.paging.insertSeparators
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.resourceUri
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.shared.domain.relation.RelationWithOrder
import kotlin.time.Instant

class RelationRepositoryImpl(
    private val lookupApi: LookupApi,
    private val relationsMetadataDao: RelationsMetadataDao,
    private val detailsMetadataDao: DetailsMetadataDao,
    private val relationDao: RelationDao,
) : RelationRepository {

    override fun insertAllUrlRelations(
        entityId: String,
        relationWithOrderList: List<RelationWithOrder>?,
        lastUpdated: Instant,
    ) {
        relationDao.insertAll(relationWithOrderList)

        // TODO: move this to entity repository
        //  and consider creating a base repository for them or interface
        detailsMetadataDao.upsert(
            entityId = entityId,
            lastUpdated = lastUpdated,
        )
    }

    override fun visited(entityId: String): Boolean =
        detailsMetadataDao.contains(entityId)

    @OptIn(ExperimentalPagingApi::class)
    override fun observeEntityRelationships(
        entity: MusicBrainzEntityType,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType>,
        query: String,
        lastUpdated: Instant,
    ): Flow<PagingData<ListItemModel>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = LookupEntityRemoteMediator(
                hasEntityBeenStored = { hasRelationsBeenSavedFor(entityId) },
                lookupEntity = { forceRefresh ->
                    lookupRelationsAndStore(
                        entity = entity,
                        entityId = entityId,
                        relatedEntities = relatedEntities,
                        forceRefresh = forceRefresh,
                        lastUpdated = lastUpdated,
                    )
                },
                deleteLocalEntity = {
                    deleteEntityRelationships(
                        entityId = entityId,
                        relatedEntities = relatedEntities,
                    )
                },
            ),
            pagingSourceFactory = {
                relationDao.getEntityRelationships(
                    entityId = entityId,
                    relatedEntities = relatedEntities,
                    query = "%$query%",
                )
            },
        ).flow
            .withDistinctLabels()
            .map { pagingData ->
                pagingData
                    .insertSeparators(
                        terminalSeparatorType = TerminalSeparatorType.SOURCE_COMPLETE,
                    ) { before: RelationListItemModel?, after: RelationListItemModel? ->
                        if (before != null && after == null) {
                            LastUpdatedFooter(lastUpdated = before.lastUpdated ?: lastUpdated)
                        } else {
                            null
                        }
                    }
            }
    }

    private fun Flow<PagingData<RelationListItemModel>>.withDistinctLabels(): Flow<PagingData<RelationListItemModel>> {
        return map { pagingData ->
            pagingData.map { relationListItem ->
                relationListItem.copy(
                    type = relationListItem.type
                        .split(", ")
                        .mapIndexed { index, part ->
                            if (index == 0 || relationListItem.type.split(", ")[index - 1] != part.trim()) {
                                part.trim()
                            } else {
                                null
                            }
                        }
                        .filterNotNull()
                        .joinToString(", "),
                )
            }
        }
    }

    private fun hasRelationsBeenSavedFor(entityId: String): Boolean {
        return relationsMetadataDao.hasRelationsBeenSavedFor(
            entityId = entityId,
        )
    }

    private suspend fun lookupRelationsAndStore(
        entity: MusicBrainzEntityType,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType>,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ) {
        if (!forceRefresh) return

        insertAllRelations(
            entity = entity,
            entityId = entityId,
            relatedEntities = relatedEntities,
            now = lastUpdated,
        )
    }

    private suspend fun insertAllRelations(
        entity: MusicBrainzEntityType,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType>,
        now: Instant,
    ) {
        val relationMusicBrainzModels = lookupEntityWithRelations(
            entityType = entity,
            entityId = entityId,
            relatedEntities = relatedEntities,
        )
        val relationWithOrderList = relationMusicBrainzModels.toRelationWithOrderList(entityId)
        relationDao.insertAll(relationWithOrderList)

        // We need to mark because an entity may have no relationships,
        // which would cause us to keep trying to fetch it from remote
        relationsMetadataDao.upsert(
            entityId = entityId,
            lastUpdated = now,
        )
    }

    @Suppress("LongMethod")
    private suspend fun lookupEntityWithRelations(
        entityType: MusicBrainzEntityType,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType>,
    ): List<RelationMusicBrainzModel>? {
        val include = relatedEntities.joinToString(separator = "+") { "${it.resourceUri}-rels" }
        return when (entityType) {
            MusicBrainzEntityType.AREA -> {
                lookupApi.lookupArea(
                    areaId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.ARTIST -> {
                lookupApi.lookupArtist(
                    artistId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.EVENT -> {
                lookupApi.lookupEvent(
                    eventId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.INSTRUMENT -> {
                lookupApi.lookupInstrument(
                    instrumentId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.LABEL -> {
                lookupApi.lookupLabel(
                    labelId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.PLACE -> {
                lookupApi.lookupPlace(
                    placeId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.RECORDING -> {
                lookupApi.lookupRecording(
                    recordingId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.RELEASE -> {
                lookupApi.lookupRelease(
                    releaseId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.RELEASE_GROUP -> {
                lookupApi.lookupReleaseGroup(
                    releaseGroupId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.SERIES -> {
                lookupApi.lookupSeries(
                    seriesId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.WORK -> {
                lookupApi.lookupWork(
                    workId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntityType.COLLECTION,
            MusicBrainzEntityType.GENRE,
            MusicBrainzEntityType.URL,
            -> error("Attempting to lookup the relationships of unsupported entity $entityType")
        }
    }

    private fun deleteEntityRelationships(
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType>,
    ) {
        relationDao.deleteRelationshipsExcludingUrlsByEntity(
            entityId = entityId,
            relatedEntities = relatedEntities,
        )
    }

    override fun getRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntityType,
    ): ImmutableList<RelationListItemModel> {
        return relationDao.getRelationshipsByType(
            entityId = entityId,
            entity = entity,
        ).toPersistentList()
    }

    override fun deleteRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntityType,
    ) {
        relationDao.deleteRelationshipsByType(
            entityId = entityId,
            entity = entity,
        )
    }

    override fun observeCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>> {
        return relationDao.observeCountOfEachRelationshipType(entityId = entityId)
    }

    override fun observeLastUpdated(entityId: String): Flow<Instant?> {
        return relationsMetadataDao.observeLastUpdated(entityId = entityId)
    }
}
