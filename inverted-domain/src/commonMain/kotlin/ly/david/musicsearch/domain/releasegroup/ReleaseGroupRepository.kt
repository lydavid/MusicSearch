package ly.david.musicsearch.domain.releasegroup

import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupScaffoldModel

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel
}
