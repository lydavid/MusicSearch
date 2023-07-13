package ly.david.data.domain.area

import ly.david.data.Area
import ly.david.data.LifeSpan
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.room.area.AreaWithCountryCodes

data class AreaScaffoldModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpan? = null,
    val countryCodes: List<String>? = null,
) : Area

internal fun AreaMusicBrainzModel.toAreaScaffoldModel() = AreaScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan,
    countryCodes = countryCodes,
)

internal fun AreaWithCountryCodes.toAreaScaffoldModel() = AreaScaffoldModel(
    id = area.id,
    name = area.name,
    disambiguation = area.disambiguation,
    type = area.type,
    lifeSpan = area.lifeSpan,
    countryCodes = countryCodes.map { it.code }
)
