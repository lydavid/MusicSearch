package ly.david.musicsearch.domain.relation.usecase

import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class LookupRelationsAndStore(
    private val relationRepository: RelationRepository,
) {
    suspend operator fun invoke(
        entity: MusicBrainzEntity,
        entityId: String,
    ) {
        relationRepository.insertAllRelationsExcludingUrls(
            entity,
            entityId,
        )
    }
}
