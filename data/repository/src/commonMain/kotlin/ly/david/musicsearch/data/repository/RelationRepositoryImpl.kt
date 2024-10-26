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
import ly.david.musicsearch.data.database.dao.VisitedDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
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

    override suspend fun insertAllRelationsExcludingUrls(
        entity: MusicBrainzEntity,
        entityId: String,
    ) {
        val relationMusicBrainzModels = lookupEntityWithRelations(
            entity = entity,
            entityId = entityId,
        )
        val relationWithOrderList = relationMusicBrainzModels.toRelationWithOrderList(entityId)
        relationDao.insertAll(relationWithOrderList)
        entityHasRelationsDao.markEntityHasRelationsStored(entityId)
    }

    private suspend fun lookupEntityWithRelations(
        entity: MusicBrainzEntity,
        entityId: String,
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

    @OptIn(ExperimentalPagingApi::class)
    override fun observeEntityRelationshipsExcludingUrls(
        entity: MusicBrainzEntity,
        entityId: String,
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
                        forceRefresh = forceRefresh,
                    )
                },
                deleteLocalEntity = {
                    deleteEntityRelationships(entityId)
                },
            ),
            pagingSourceFactory = {
                relationDao.getEntityRelationshipsExcludingUrls(
                    entityId = entityId,
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
        forceRefresh: Boolean,
    ) {
        if (!forceRefresh) return

        insertAllRelationsExcludingUrls(
            entity = entity,
            entityId = entityId,
        )
    }

    private fun deleteEntityRelationships(
        entityId: String,
    ) {
        relationDao.deleteRelationshipsExcludingUrlsByEntity(entityId)
    }

    override fun getEntityUrlRelationships(
        entityId: String,
    ): List<RelationListItemModel> = relationDao.getEntityUrlRelationships(
        entityId = entityId,
    )

    override fun deleteUrlRelationshipsByEntity(entityId: String) {
        relationDao.deleteUrlRelationshipsByEntity(entityId)
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
