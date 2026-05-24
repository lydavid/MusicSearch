package ly.david.musicsearch.shared.domain.releasegroup

import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import kotlin.time.Instant

interface ReleaseGroupRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ReleaseGroupDetailsModel
}
