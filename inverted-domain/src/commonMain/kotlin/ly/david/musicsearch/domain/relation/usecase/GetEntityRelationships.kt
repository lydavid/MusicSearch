package ly.david.musicsearch.domain.relation.usecase

import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class GetEntityRelationships(
    private val relationRepository: RelationRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
        entityId: String,
        query: String,
    ) = relationRepository.observeEntityRelationshipsExcludingUrls(
        entity = entity,
        entityId = entityId,
        query = query,
    )
}
