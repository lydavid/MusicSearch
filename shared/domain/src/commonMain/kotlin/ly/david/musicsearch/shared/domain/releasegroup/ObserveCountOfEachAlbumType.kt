package ly.david.musicsearch.shared.domain.releasegroup

import kotlinx.coroutines.flow.Flow

interface ObserveCountOfEachAlbumType {
    operator fun invoke(
        entityId: String,
        isCollection: Boolean,
    ): Flow<List<ReleaseGroupTypeCount>>
}
