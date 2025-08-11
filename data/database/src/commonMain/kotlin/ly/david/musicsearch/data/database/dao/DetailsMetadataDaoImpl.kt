package ly.david.musicsearch.data.database.dao

import kotlin.time.Instant
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao

class DetailsMetadataDaoImpl(
    private val database: Database,
) : DetailsMetadataDao {
    override fun upsert(
        entityId: String,
        lastUpdated: Instant,
    ) {
        database.details_metadataQueries.upsert(
            lastUpdated = lastUpdated,
            entityId = entityId,
        )
    }

    override fun contains(entityId: String): Boolean {
        return database.details_metadataQueries.contains(entityId = entityId)
            .executeAsOneOrNull() != null
    }
}
