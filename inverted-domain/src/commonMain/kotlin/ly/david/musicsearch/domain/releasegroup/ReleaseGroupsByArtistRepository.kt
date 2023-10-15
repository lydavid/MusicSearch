package ly.david.musicsearch.domain.releasegroup

import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel

interface ReleaseGroupsByArtistRepository {
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel
}
