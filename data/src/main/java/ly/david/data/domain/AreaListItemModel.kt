package ly.david.data.domain

import ly.david.data.Area
import ly.david.data.LifeSpan
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.persistence.area.AreaRoomModel
import ly.david.data.persistence.release.AreaWithReleaseDate

data class AreaListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpan? = null,
    val countryCodes: List<String>? = null,
    val date: String? = null
) : Area, ListItemModel()

internal fun AreaMusicBrainzModel.toAreaListItemModel(date: String? = null) = AreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan,
    countryCodes = countryCodes,
    date = date
)

fun AreaRoomModel.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan,
)

fun AreaWithReleaseDate.toAreaListItemModel() = AreaListItemModel(
    id = area.id,
    name = area.name,
    sortName = area.sortName,
    disambiguation = area.disambiguation,
    type = area.type,
    lifeSpan = area.lifeSpan,
    countryCodes = countryCodes.map { it.code },
    date = releaseCountry.date
)
