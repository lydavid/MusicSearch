package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Coordinates
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.Place
import ly.david.mbjc.data.network.PlaceMusicBrainzModel

internal data class PlaceUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val address: String,
    override val type: String? = null,
//    override val typeId: String?,
    override val coordinates: Coordinates? = null,
    override val lifeSpan: LifeSpan? = null,

    val area: AreaUiModel? = null,
) : Place, UiModel()

internal fun PlaceMusicBrainzModel.toPlaceUiModel() =
    PlaceUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = coordinates,
        lifeSpan = lifeSpan,
        area = area?.toAreaUiModel()
    )
