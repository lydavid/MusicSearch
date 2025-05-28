package ly.david.musicsearch.shared.domain.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

interface DetailsMetadataDao {
    fun upsert(
        entityId: String,
        lastUpdated: Instant = Clock.System.now(),
    )

    fun contains(entityId: String): Boolean
}
