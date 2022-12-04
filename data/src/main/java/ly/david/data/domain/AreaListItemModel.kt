package ly.david.data.domain

import ly.david.data.Area
import ly.david.data.LifeSpan
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.persistence.release.AreaWithReleaseDate

data class AreaListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpan? = null,
    val iso_3166_1_codes: List<String>? = null,
    val date: String? = null
) : Area, ListItemModel()

internal fun AreaMusicBrainzModel.toAreaListItemModel(date: String? = null) = AreaListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan,
    iso_3166_1_codes = iso_3166_1_codes,
    date = date
)

fun AreaWithReleaseDate.toAreaListItemModel() = AreaListItemModel(
    id = area.id,
    name = area.name,
    disambiguation = area.disambiguation,
    type = area.type,
    lifeSpan = area.lifeSpan,
    iso_3166_1_codes = countryCodes.map { it.code },
    date = releaseCountry.date
)
