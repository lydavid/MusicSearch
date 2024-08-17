package ly.david.musicsearch.shared.domain.collection.usecase

import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.collection.CollectionRepository

class CreateCollection(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(
        name: String,
        entity: MusicBrainzEntity,
    ) = collectionRepository.insertLocal(
        CollectionListItemModel(
            id = getUUID(),
            name = name,
            entity = entity,
            entityCount = 0,
            isRemote = false,
        ),
    )
}

expect fun getUUID(): String
