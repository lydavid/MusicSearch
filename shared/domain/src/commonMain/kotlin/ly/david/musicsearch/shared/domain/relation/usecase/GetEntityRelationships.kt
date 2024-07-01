package ly.david.musicsearch.shared.domain.relation.usecase

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository
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
