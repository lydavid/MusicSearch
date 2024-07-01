package ly.david.musicsearch.shared.domain.releasegroup

import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel
}
