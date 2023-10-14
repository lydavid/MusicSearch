package ly.david.musicsearch.data.repository

import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.listitem.RelationWithOrder
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.relation.RelationTypeCount
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EntityHasUrlsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
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
        relationDao.insertAll(relationWithOrderList)
        entityHasUrlsDao.markEntityHasUrls(entityId)
    }

    override suspend fun insertAllRelationsExcludingUrls(
        entity: MusicBrainzEntity,
        entityId: String,
    ) {
        val relationMusicBrainzModels = lookupEntityWithRelations(
            entity = entity,
            entityId = entityId
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
