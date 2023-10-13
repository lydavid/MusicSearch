package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.listitem.LabelListItemModel
import lydavidmusicsearchdatadatabase.Label

fun Label.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = label_code,
    )
