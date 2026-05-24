package ly.david.musicsearch.shared.domain.area

import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel

interface AreaRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): AreaDetailsModel
}
