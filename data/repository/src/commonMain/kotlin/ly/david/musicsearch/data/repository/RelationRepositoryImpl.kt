package ly.david.musicsearch.data.repository

import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.listitem.RelationWithOrder
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.relation.RelationTypeCount
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EntityHasUrlsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.domain.relation.RelationRepository
import lydavidmusicsearchdatadatabase.CountOfEachRelationshipType

class RelationRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val entityHasRelationsDao: EntityHasRelationsDao,
    private val entityHasUrlsDao: EntityHasUrlsDao,
    private val relationDao: RelationDao,
) : RelationRepository {
    override fun hasUrlsBeenSavedFor(entityId: String): Boolean =
        entityHasUrlsDao.hasUrls(entityId)

    override fun insertAllUrlRelations(
        entityId: String,
        relationWithOrderList: List<RelationWithOrder>?,
    ) {
        insertRelations(entityId, relationWithOrderList)
        entityHasUrlsDao.markEntityHasUrls(entityId)
    }

    override suspend fun insertAllRelationsExcludingUrls(
        entity: MusicBrainzEntity,
        entityId: String,
//        relationMusicBrainzModels: List<RelationWithOrder>?,
    ) {
//        val relations = relationsListRepository.lookupRelationsFromNetwork(entityId)
//        val relationWithOrderList = relations?.mapIndexedNotNull { index, relationMusicBrainzModel ->
//            relationMusicBrainzModel.toRelationDatabaseModel(
//                entityId = entityId,
//                order = index,
//            )
//        }
//        relationRepository.insertAllRelationsExcludingUrls(
//            entityId = entityId,
//            relationMusicBrainzModels = relationWithOrderList,
//        )
        val relationWithOrderList = lookupEntityWithRelations(entity, entityId)
        insertRelations(entityId, relationWithOrderList)
        entityHasRelationsDao.markEntityHasRelationsStored(entityId)
    }

    override fun insertRelations(
        entityId: String,
        relationMusicBrainzModels: List<RelationWithOrder>?,
    ) {
        relationDao.insertAll(relationMusicBrainzModels)
    }

    private suspend fun lookupEntityWithRelations(
        entity: MusicBrainzEntity,
        entityId: String,
    ): List<RelationWithOrder> {
        when (entity) {
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
            MusicBrainzEntity.COLLECTION -> TODO()
            MusicBrainzEntity.EVENT -> TODO()
            MusicBrainzEntity.GENRE -> TODO()
            MusicBrainzEntity.INSTRUMENT -> TODO()
            MusicBrainzEntity.LABEL -> TODO()
            MusicBrainzEntity.PLACE -> TODO()
            MusicBrainzEntity.RECORDING -> TODO()
            MusicBrainzEntity.RELEASE -> TODO()
            MusicBrainzEntity.RELEASE_GROUP -> TODO()
            MusicBrainzEntity.SERIES -> TODO()
            MusicBrainzEntity.WORK -> TODO()
            MusicBrainzEntity.URL -> TODO()
        }
    }

//    private suspend fun lookupEntityWithUrls(
//        entity: MusicBrainzEntity,
//        entityId: String,
//    ): List<RelationWithOrder> {
//
//    }

    override fun hasRelationsBeenSavedFor(entityId: String): Boolean {
        return entityHasRelationsDao.hasRelationsBeenSavedFor(
            entityId = entityId,
        )
    }

    override fun getEntityRelationshipsExcludingUrls(
        entityId: String,
        query: String,
    ): PagingSource<Int, RelationListItemModel> {
        return relationDao.getEntityRelationshipsExcludingUrls(
            entityId = entityId,
            query = "%$query%",
        )
    }

    override fun getEntityUrlRelationships(
        entityId: String,
    ): List<RelationListItemModel> = relationDao.getEntityUrlRelationships(
        entityId = entityId,
    )

    override fun deleteEntityRelationships(
        entityId: String,
    ) {
        relationDao.deleteRelationshipsExcludingUrlsByEntity(entityId)
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
