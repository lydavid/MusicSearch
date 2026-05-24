package ly.david.musicsearch.shared.domain.work

import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import kotlin.time.Instant

interface WorkRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): WorkDetailsModel
}
