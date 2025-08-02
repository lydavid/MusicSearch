package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers

class RelationsMetadataDao(
    private val database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
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

    fun observeLastUpdated(entityId: String): Flow<Instant?> {
        return database.relations_metadataQueries.getLastUpdated(entityId = entityId)
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io)
    }
}
