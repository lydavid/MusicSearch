package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.musicsearch.data.core.listitem.WorkListItemModel
import ly.david.musicsearch.domain.work.toWorkAttributeUiModel
import lydavidmusicsearchdatadatabase.Work

internal fun WorkMusicBrainzModel.toWorkListItemModel() =
    WorkListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
        attributes = attributes?.map { it.toWorkAttributeUiModel() }.orEmpty()
    )

fun Work.toWorkListItemModel() =
    WorkListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
        attributes = listOf()
    )
