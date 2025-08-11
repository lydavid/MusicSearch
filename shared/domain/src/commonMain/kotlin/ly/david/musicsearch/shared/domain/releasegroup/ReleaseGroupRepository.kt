package ly.david.musicsearch.shared.domain.releasegroup

import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ReleaseGroupDetailsModel
}
