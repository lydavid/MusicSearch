package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Label
import ly.david.data.LifeSpan

data class LabelInfo(
    @Json(name = "catalog-number") val catalogNumber: String? = null,
    @Json(name = "label") val label: LabelMusicBrainzModel? = null,
)

data class LabelMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "name") override val name: String,
    @Json(name = "type") override val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "label-code") override val labelCode: Int? = null,
    @Json(name = "area") val area: AreaMusicBrainzModel? = null,
    @Json(name = "life-span") val lifeSpan: LifeSpan? = null,
    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Label
