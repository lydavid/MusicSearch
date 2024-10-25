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

    fun visited(entityId: String): Boolean {
        return database.visitedQueries.hasUrls(entityId = entityId)
            .executeAsOneOrNull() != null
    }
}
