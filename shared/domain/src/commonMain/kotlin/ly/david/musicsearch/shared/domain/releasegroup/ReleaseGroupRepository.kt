package ly.david.musicsearch.shared.domain.releasegroup

import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupDetailsModel
}
