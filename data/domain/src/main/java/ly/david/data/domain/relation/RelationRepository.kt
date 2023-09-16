package ly.david.data.domain.relation

import app.cash.paging.PagingSource
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EntityHasUrlsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.toRelationDatabaseModel
import lydavidmusicsearchdatadatabase.Mb_relation
import org.koin.core.annotation.Single

@Single
class RelationRepository(
    private val entityHasRelationsDao: EntityHasRelationsDao,
    private val entityHasUrlsDao: EntityHasUrlsDao,
    private val relationDao: RelationDao,
) {
    suspend fun hasUrlsBeenSavedFor(entityId: String): Boolean =
        entityHasUrlsDao.hasUrls(entityId)

    suspend fun insertAllUrlRelations(entityId: String, relationMusicBrainzModels: List<RelationMusicBrainzModel>?) {
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

    fun getEntityRelationships(
        entityId: String,
        query: String,
    ): PagingSource<Int, Mb_relation> {
        return relationDao.getEntityRelationshipsExcludingUrls(
            entityId = entityId,
            query = "%$query%",
        )
    }

    fun deleteEntityRelationships(
        entityId: String,
    ) {
        relationDao.deleteRelationshipsExcludingUrlsByEntity(entityId)
    }
}
