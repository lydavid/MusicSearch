package ly.david.musicsearch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.musicbrainz.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.listitem.RelationListItemModel
import ly.david.musicsearch.core.models.relation.RelationWithOrder
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.relation.RelationTypeCount
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EntityHasUrlsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.relation.RelationRepository
import lydavidmusicsearchdatadatabase.CountOfEachRelationshipType

class RelationRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val entityHasRelationsDao: EntityHasRelationsDao,
    private val entityHasUrlsDao: EntityHasUrlsDao,
    private val relationDao: RelationDao,
) : RelationRepository {
    override fun insertAllUrlRelations(
        entityId: String,
        relationWithOrderList: List<RelationWithOrder>?,
    ) {
        relationDao.insertAll(relationWithOrderList)
        entityHasUrlsDao.markEntityHasUrls(entityId)
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
                musicBrainzApi.lookupArea(
                    areaId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.ARTIST -> {
                musicBrainzApi.lookupArtist(
                    artistId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.EVENT -> {
                musicBrainzApi.lookupEvent(
                    eventId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.INSTRUMENT -> {
                musicBrainzApi.lookupInstrument(
                    instrumentId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.LABEL -> {
                musicBrainzApi.lookupLabel(
                    labelId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.PLACE -> {
                musicBrainzApi.lookupPlace(
                    placeId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_EVENTS_URLS,
                ).relations
            }

            MusicBrainzEntity.RECORDING -> {
                musicBrainzApi.lookupRecording(
                    recordingId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.RELEASE -> {
                musicBrainzApi.lookupRelease(
                    releaseId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                musicBrainzApi.lookupReleaseGroup(
                    releaseGroupId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.SERIES -> {
                musicBrainzApi.lookupSeries(
                    seriesId = entityId,
                    include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
                ).relations
            }

            MusicBrainzEntity.WORK -> {
                musicBrainzApi.lookupWork(
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

    override fun hasUrlsBeenSavedFor(entityId: String): Boolean =
        entityHasUrlsDao.hasUrls(entityId)

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
