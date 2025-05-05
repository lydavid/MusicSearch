package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import lydavidmusicsearchdatadatabase.Collection_entity

class CollectionEntityDao(
    database: Database,
) : EntityDao {
    override val transacter = database.collection_entityQueries

    @Suppress("SwallowedException")
    fun insert(
        collectionId: String,
        entityId: String,
    ): Long {
        return try {
            transacter.insertOrFail(
                Collection_entity(
                    id = collectionId,
                    entity_id = entityId,
                    deleted = false,
                ),
            )
            1
        } catch (ex: Exception) {
            INSERTION_FAILED_DUE_TO_CONFLICT
        }
    }

    fun insertAll(
        collectionId: String,
        entityIds: List<String>,
    ) {
        return transacter.transaction {
            entityIds.forEach { entityId ->
                insert(
                    collectionId = collectionId,
                    entityId = entityId,
                )
            }
        }
    }

    fun deleteEntityLinksFromCollection(collectionId: String) {
        transacter.deleteEntitiyLinksFromCollection(collectionId)
    }

    fun markDeletedFromCollection(
        collectionId: String,
        collectableIds: Set<String>,
    ) {
        transacter.transaction {
            collectableIds.forEach { collectableId ->
                transacter.markDeletedFromCollection(
                    collectionId = collectionId,
                    collectableId = collectableId,
                )
            }
        }
    }

    fun unMarkDeletedFromCollection(
        collectionId: String,
    ) {
        transacter.unMarkDeletedFromCollection(collectionId = collectionId)
    }

    fun getIdsMarkedForDeletionFromCollection(
        collectionId: String,
    ): Set<String> {
        return transacter.getIdsMarkedForDeletionFromCollection(collectionId)
            .executeAsList()
            .toSet()
    }

    fun deleteFromCollection(
        collectionId: String,
    ) {
        transacter.deleteFromCollection(
            collectionId = collectionId,
        )
    }

    fun deleteCollection(
        collectionId: String,
    ) {
        transacter.deleteCollection(collectionId)
    }

    fun getCountOfEntitiesByCollection(collectionId: String): Int =
        transacter.getCountOfEntitiesByCollection(
            collectionId = collectionId,
        )
            .executeAsOne()
            .toInt()
}
