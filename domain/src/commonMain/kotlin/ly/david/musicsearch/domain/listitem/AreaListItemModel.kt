package ly.david.musicsearch.domain.listitem

import ly.david.data.core.area.ReleaseEvent
import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.musicsearch.domain.common.toLifeSpanUiModel
import lydavidmusicsearchdatadatabase.Area

data class AreaListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpanUiModel? = null,
    val countryCodes: List<String>? = null,
    val date: String? = null,
) : ly.david.data.core.area.Area, ListItemModel()

internal fun AreaMusicBrainzModel.toAreaListItemModel(date: String? = null) = AreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    countryCodes = countryCodes,
    date = date
)

fun Area.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    sortName = sort_name,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
)

fun ReleaseEvent.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    date = date,
    countryCodes = countryCode?.let { listOf(it) },
)
