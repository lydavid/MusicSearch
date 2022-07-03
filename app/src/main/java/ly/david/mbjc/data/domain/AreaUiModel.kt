package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Area
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.network.AreaMusicBrainzModel

internal data class AreaUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String?,
    override val type: String?,
    override val lifeSpan: LifeSpan?
) : Area, UiModel()

internal fun AreaMusicBrainzModel.toAreaUiModel() =
    AreaUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan
    )
