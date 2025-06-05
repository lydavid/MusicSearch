package ly.david.musicsearch.shared.domain.releasegroup

import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        forceRefresh: Boolean,
    ): ReleaseGroupDetailsModel
}
