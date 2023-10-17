package ly.david.musicsearch.domain.releasegroup

import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel
}
