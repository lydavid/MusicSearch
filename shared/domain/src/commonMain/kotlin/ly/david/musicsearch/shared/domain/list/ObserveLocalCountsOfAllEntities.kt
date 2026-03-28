package ly.david.musicsearch.shared.domain.list

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface ObserveLocalCountsOfAllEntities {
    operator fun invoke(): Flow<List<Pair<MusicBrainzEntityType, Long>>>
}
