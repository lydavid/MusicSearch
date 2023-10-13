package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.listitem.CollectionListItemModel
import lydavidmusicsearchdatadatabase.Collection

fun Collection.toCollectionListItemModel() =
    CollectionListItemModel(
        id = id,
        isRemote = is_remote,
        name = name,
        entity = entity,
        entityCount = entity_count,
    )
