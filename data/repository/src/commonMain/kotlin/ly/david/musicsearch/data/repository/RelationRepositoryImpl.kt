package ly.david.musicsearch.data.repository

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.relation.RelationWithOrder
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.network.resourceUri
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import lydavidmusicsearchdatadatabase.CountOfEachRelationshipType

class RelationRepositoryImpl(
    private val lookupApi: LookupApi,
    private val entityHasRelationsDao: EntityHasRelationsDao,
    private val visitedDao: VisitedDao,
    private val relationDao: RelationDao,
) : RelationRepository {
    override fun insertAllUrlRelations(
        entityId: String,
        relationWithOrderList: List<RelationWithOrder>?,
    ) {
        relationDao.insertAll(relationWithOrderList)
        visitedDao.insert(entityId)
    }

    override suspend fun insertAllRelations(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity>,
    ) {
        val relationMusicBrainzModels = lookupEntityWithRelations(
            entity = entity,
            entityId = entityId,
            relatedEntities = relatedEntities,
        )
        val relationWithOrderList = relationMusicBrainzModels.toRelationWithOrderList(entityId)
        relationDao.insertAll(relationWithOrderList)

        // We need to mark because an entity may have no relationships,
        // which would cause us to keep trying to fetch it from remote
        entityHasRelationsDao.markEntityHasRelationsStored(entityId)
    }

    private suspend fun lookupEntityWithRelations(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity>,
    ): List<RelationMusicBrainzModel>? {
        val include = relatedEntities.joinToString(separator = "+") { "${it.resourceUri}-rels" }
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                lookupApi.lookupArea(
                    areaId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.ARTIST -> {
                lookupApi.lookupArtist(
                    artistId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.EVENT -> {
                lookupApi.lookupEvent(
                    eventId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.INSTRUMENT -> {
                lookupApi.lookupInstrument(
                    instrumentId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.LABEL -> {
                lookupApi.lookupLabel(
                    labelId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.PLACE -> {
                lookupApi.lookupPlace(
                    placeId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.RECORDING -> {
                lookupApi.lookupRecording(
                    recordingId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.RELEASE -> {
                lookupApi.lookupRelease(
                    releaseId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                lookupApi.lookupReleaseGroup(
                    releaseGroupId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.SERIES -> {
                lookupApi.lookupSeries(
                    seriesId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.WORK -> {
                lookupApi.lookupWork(
                    workId = entityId,
                    include = include,
                ).relations
            }

            MusicBrainzEntity.COLLECTION,
            MusicBrainzEntity.GENRE,
            MusicBrainzEntity.URL,
            -> error("Attempting to lookup the relationships of unsupported entity $entity")
        }
    }

    override fun visited(entityId: String): Boolean =
        visitedDao.contains(entityId)

    @OptIn(ExperimentalPagingApi::class)
    override fun observeEntityRelationships(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity>,
        query: String,
    ): Flow<PagingData<RelationListItemModel>> {
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
    }

    private fun hasRelationsBeenSavedFor(entityId: String): Boolean {
        return entityHasRelationsDao.hasRelationsBeenSavedFor(
            entityId = entityId,
        )
    }

    private suspend fun lookupRelationsAndStore(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity>,
        forceRefresh: Boolean,
    ) {
        if (!forceRefresh) return

        insertAllRelations(
            entity = entity,
            entityId = entityId,
            relatedEntities = relatedEntities,
        )
    }

    private fun deleteEntityRelationships(
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity>,
    ) {
        relationDao.deleteRelationshipsExcludingUrlsByEntity(
            entityId = entityId,
            relatedEntities = relatedEntities,
        )
    }

    override fun getRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntity,
    ): List<RelationListItemModel> = relationDao.getRelationshipsByType(
        entityId = entityId,
        entity = entity,
    )

    override fun deleteRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        relationDao.deleteRelationshipsByType(
            entityId = entityId,
            entity = entity,
        )
    }

    override fun getCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>> =
        relationDao.getCountOfEachRelationshipType(entityId).map {
            it.map { countOfEachRelationshipType: CountOfEachRelationshipType ->
                RelationTypeCount(
                    linkedEntity = countOfEachRelationshipType.linked_entity,
                    count = countOfEachRelationshipType.entity_count.toInt(),
                )
            }
        }
}
