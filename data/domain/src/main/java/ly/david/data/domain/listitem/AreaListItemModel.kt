package ly.david.data.domain.listitem

import ly.david.data.domain.common.LifeSpanUiModel
import ly.david.data.domain.common.toLifeSpanUiModel
import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.room.area.AreaRoomModel
import ly.david.data.room.release.AreaWithReleaseDate
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
) : ly.david.data.core.Area, ListItemModel()

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

fun AreaRoomModel.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan?.toLifeSpanUiModel(),
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

fun AreaWithReleaseDate.toAreaListItemModel() = AreaListItemModel(
    id = area.id,
    name = area.name,
    sortName = area.sortName,
    disambiguation = area.disambiguation,
    type = area.type,
    lifeSpan = area.lifeSpan?.toLifeSpanUiModel(),
    countryCodes = countryCodes.map { it.code },
    date = releaseCountry.date
)
