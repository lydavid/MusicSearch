package ly.david.data.domain

import ly.david.data.Area
import ly.david.data.LifeSpan
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.persistence.area.AreaWithIso

data class AreaScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpan? = null,
    val iso_3166_1_codes: List<String>? = null
) : Area

internal fun AreaMusicBrainzModel.toAreaScaffoldModel() = AreaScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan,
    iso_3166_1_codes = iso_3166_1_codes,
)

internal fun AreaWithIso.toAreaScaffoldModel() = AreaScaffoldModel(
    id = area.id,
    name = area.name,
    disambiguation = area.disambiguation,
    type = area.type,
    lifeSpan = area.lifeSpan,
    iso_3166_1_codes = countryCodes.map { it.code }
)
