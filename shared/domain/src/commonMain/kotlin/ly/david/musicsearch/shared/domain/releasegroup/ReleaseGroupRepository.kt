package ly.david.musicsearch.shared.domain.releasegroup

import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupDetailsModel

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupDetailsModel
}
