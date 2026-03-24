package ly.david.musicsearch.shared.domain.list

import kotlinx.coroutines.flow.Flow

interface ObserveTrackCount {
    operator fun invoke(releaseId: String): Flow<Int>
}
