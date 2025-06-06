package ly.david.musicsearch.data.database.dao

import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.Database

class RelationsMetadataDao(
    private val database: Database,
) {
    fun upsert(
        entityId: String,
        lastUpdated: Instant,
    ) {
        database.relations_metadataQueries.upsert(
            entityId = entityId,
            lastUpdated = lastUpdated,
        )
    }

    fun hasRelationsBeenSavedFor(entityId: String): Boolean {
        return database.relations_metadataQueries.contains(entityId = entityId)
            .executeAsOneOrNull() != null
    }
}
