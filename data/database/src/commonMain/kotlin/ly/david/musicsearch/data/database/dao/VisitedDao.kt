package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database

class VisitedDao(
    private val database: Database,
) {
    fun insert(entityId: String) {
        database.visitedQueries.insert(
            entity_id = entityId,
        )
    }

    fun contains(entityId: String): Boolean {
        return database.visitedQueries.contains(entityId = entityId)
            .executeAsOneOrNull() != null
    }
}
