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
        relatedEntities: List<MusicBrainzEntity>,
    ) {
        val relationMusicBrainzModels = lookupEntityWithRelations(
            entity = entity,
            entityId = entityId,
            relatedEntities = relatedEntities,
        )
        val relationWithOrderList = relationMusicBrainzModels.toRelationWithOrderList(entityId)
        relationDao.insertAll(relationWithOrderList)
        entityHasRelationsDao.markEntityHasRelationsStored(entityId)
    }

    // TODO: build include out of relatedEntities
    private suspend fun lookupEntityWithRelations(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: List<MusicBrainzEntity>,
    ): List<RelationMusicBrainzModel>? {
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                lookupApi.lookupArea(
                    areaId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.ARTIST -> {
                lookupApi.lookupArtist(
                    artistId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.EVENT -> {
                lookupApi.lookupEvent(
                    eventId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.INSTRUMENT -> {
                lookupApi.lookupInstrument(
                    instrumentId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.LABEL -> {
                lookupApi.lookupLabel(
                    labelId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.PLACE -> {
                lookupApi.lookupPlace(
                    placeId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_EVENTS_URLS,
                ).relations
            }

            MusicBrainzEntity.RECORDING -> {
                lookupApi.lookupRecording(
                    recordingId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.RELEASE -> {
                lookupApi.lookupRelease(
                    releaseId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                lookupApi.lookupReleaseGroup(
                    releaseGroupId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.SERIES -> {
                lookupApi.lookupSeries(
                    seriesId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.WORK -> {
                lookupApi.lookupWork(
                    workId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
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

    // TODO: reuse this one implementation
    //  pass in a list of entities that
    //  - we fetch from network
    //  - store
    //  - query
    //  - delete
    @OptIn(ExperimentalPagingApi::class)
    override fun observeEntityRelationships(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: List<MusicBrainzEntity>,
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
        relatedEntities: List<MusicBrainzEntity>,
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
        relatedEntities: List<MusicBrainzEntity>,
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
