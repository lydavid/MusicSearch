package ly.david.data.domain.relation

import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.room.relation.RelationTypeCount
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EntityHasUrlsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.toRelationDatabaseModel
import lydavidmusicsearchdatadatabase.CountOfEachRelationshipType
import lydavidmusicsearchdatadatabase.Mb_relation
import org.koin.core.annotation.Single

@Single
class RelationRepository(
    private val entityHasRelationsDao: EntityHasRelationsDao,
    private val entityHasUrlsDao: EntityHasUrlsDao,
    private val relationDao: RelationDao,
) {
    fun hasUrlsBeenSavedFor(entityId: String): Boolean =
        entityHasUrlsDao.hasUrls(entityId)

    fun insertAllUrlRelations(entityId: String, relationMusicBrainzModels: List<RelationMusicBrainzModel>?) {
        insertRelations(entityId, relationMusicBrainzModels)
        entityHasUrlsDao.markEntityHasUrls(entityId)
    }

    fun insertAllRelationsExcludingUrls(entityId: String, relationMusicBrainzModels: List<RelationMusicBrainzModel>?) {
        insertRelations(entityId, relationMusicBrainzModels)
        entityHasRelationsDao.markEntityHasRelationsStored(entityId)
    }

    private fun insertRelations(entityId: String, relationMusicBrainzModels: List<RelationMusicBrainzModel>?) {
        val relationRoomModels = mutableListOf<Mb_relation>()
        relationMusicBrainzModels?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationDatabaseModel(
                entityId = entityId,
                order = index
            )?.let { relationRoomModel ->
                relationRoomModels.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relationRoomModels)
    }

    fun hasRelationsBeenSavedFor(entityId: String): Boolean {
        return entityHasRelationsDao.hasRelationsBeenSavedFor(
            entityId = entityId,
        )
    }

    fun getEntityRelationshipsExcludingUrls(
        entityId: String,
        query: String,
    ): PagingSource<Int, Mb_relation> {
        return relationDao.getEntityRelationshipsExcludingUrls(
            entityId = entityId,
            query = "%$query%",
        )
    }

    fun getEntityUrlRelationships(
        entityId: String,
    ) = relationDao.getEntityUrlRelationships(
        entityId = entityId,
    )

    fun deleteEntityRelationships(
        entityId: String,
    ) {
        relationDao.deleteRelationshipsExcludingUrlsByEntity(entityId)
    }

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>> =
        relationDao.getCountOfEachRelationshipType(entityId).map {
            it.map { countOfEachRelationshipType: CountOfEachRelationshipType ->
                RelationTypeCount(
                    linkedEntity = countOfEachRelationshipType.linked_entity,
                    count = countOfEachRelationshipType.entity_count.toInt(),
                )
            }
        }
}
