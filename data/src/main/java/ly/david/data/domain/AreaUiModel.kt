package ly.david.data.domain

import ly.david.data.Area
import ly.david.data.AreaType
import ly.david.data.LifeSpan
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.persistence.area.AreaRoomModel

data class AreaUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpan? = null,
    val iso_3166_1_codes: List<String>? = null,
) : Area, UiModel()

internal fun AreaMusicBrainzModel.toAreaUiModel() =
    AreaUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan,
        iso_3166_1_codes = iso_3166_1_codes
    )

fun AreaRoomModel.toAreaUiModel(iso_3166_1_codes: List<String>? = null) =
    AreaUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan,
        iso_3166_1_codes = iso_3166_1_codes
    )

fun AreaUiModel.showReleases(): Boolean =
    type == AreaType.COUNTRY || name == AreaType.WORLDWIDE
