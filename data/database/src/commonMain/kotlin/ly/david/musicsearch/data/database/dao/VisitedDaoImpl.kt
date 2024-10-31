package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.history.VisitedDao

class VisitedDaoImpl(
    private val database: Database,
) : VisitedDao {
    override fun insert(entityId: String) {
        database.visitedQueries.insert(
            entity_id = entityId,
        )
    }

    override fun contains(entityId: String): Boolean {
        return database.visitedQueries.contains(entityId = entityId)
            .executeAsOneOrNull() != null
    }
}
