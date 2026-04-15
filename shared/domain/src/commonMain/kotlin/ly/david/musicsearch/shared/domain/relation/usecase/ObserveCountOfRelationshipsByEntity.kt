package ly.david.musicsearch.shared.domain.relation.usecase

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface ObserveCountOfRelationshipsByEntity {
    operator fun invoke(
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType>,
        query: String,
    ): Flow<Int>
}
