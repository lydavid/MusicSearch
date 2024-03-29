package ly.david.musicsearch.domain.collection.usecase

import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.collection.CollectionRepository
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class CreateCollection(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(
        name: String,
        entity: MusicBrainzEntity,
    ) = collectionRepository.insertLocal(
        CollectionListItemModel(
            id = UUID.randomUUID().toString(),
            name = name,
            entity = entity,
            entityCount = 1,
            isRemote = false,
        ),
    )
}
