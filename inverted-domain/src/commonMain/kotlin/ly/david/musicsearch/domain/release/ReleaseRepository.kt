package ly.david.musicsearch.domain.release

import ly.david.musicsearch.data.core.release.ReleaseScaffoldModel

interface ReleaseRepository {
    suspend fun lookupRelease(releaseId: String): ReleaseScaffoldModel
}
