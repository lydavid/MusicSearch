package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.musicsearch.data.core.listitem.LabelListItemModel
import lydavidmusicsearchdatadatabase.Label

internal fun LabelMusicBrainzModel.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )

fun Label.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = label_code,
    )
