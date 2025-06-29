package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Collection_entity

class CollectionEntityDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.collection_entityQueries

    private fun addToCollection(
        collectionId: String,
        entityId: String,
    ): Long {
        return transacter.insert(
            Collection_entity(
                id = collectionId,
                entity_id = entityId,
                deleted = false,
            ),
        ).value
    }

    fun addAllToCollection(
        collectionId: String,
        entityIds: List<String>,
    ): Long {
        return transacter.transactionWithResult {
            entityIds.sumOf { entityId ->
                addToCollection(
                    collectionId = collectionId,
                    entityId = entityId,
                )
            }
        }
    }

    fun deleteAllFromCollection(collectionId: String) {
        transacter.deleteAllFromCollection(collectionId = collectionId)
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
        return transacter.getIdsMarkedForDeletionFromCollection(collectionId = collectionId)
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
        transacter.deleteCollection(collectionId = collectionId)
    }

    fun getCountOfEntitiesByCollection(collectionId: String): Int =
        transacter.getCountOfEntitiesByCollection(
            collectionId = collectionId,
        )
            .executeAsOne()
            .toInt()

    fun observeCountOfEntitiesByCollection(collectionId: String): Flow<Int> =
        transacter.getCountOfEntitiesByCollection(
            collectionId = collectionId,
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun entityIsInACollection(entityId: String): Flow<Boolean> =
        transacter.entityIsInACollection(
            entityId = entityId,
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
}
