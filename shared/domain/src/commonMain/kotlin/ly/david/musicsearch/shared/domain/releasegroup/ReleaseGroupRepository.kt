package ly.david.musicsearch.shared.domain.releasegroup

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupDetailsModel
}
