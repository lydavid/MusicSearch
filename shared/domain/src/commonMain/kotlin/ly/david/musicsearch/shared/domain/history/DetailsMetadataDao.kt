package ly.david.musicsearch.shared.domain.history

import kotlin.time.Clock
import kotlin.time.Instant

interface DetailsMetadataDao {
    fun upsert(
        entityId: String,
        lastUpdated: Instant = Clock.System.now(),
    )

    fun contains(entityId: String): Boolean
}
