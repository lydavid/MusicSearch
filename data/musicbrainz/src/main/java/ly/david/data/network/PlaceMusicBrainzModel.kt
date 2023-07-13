package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Coordinates
import ly.david.data.LifeSpan
import ly.david.data.Place

data class PlaceMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "address") override val address: String = "",
    @Json(name = "type") override val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "coordinates") override val coordinates: Coordinates? = null,
    @Json(name = "life-span") override val lifeSpan: LifeSpan? = null,

    @Json(name = "area") val area: AreaMusicBrainzModel? = null,
    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), Place
