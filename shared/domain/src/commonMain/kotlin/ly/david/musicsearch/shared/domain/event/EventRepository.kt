package ly.david.musicsearch.shared.domain.event

import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.details.EventDetailsModel

interface EventRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): EventDetailsModel
}
