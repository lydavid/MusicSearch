package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Area
import ly.david.data.LifeSpan

data class AreaMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "type") override val type: String? = null,

    // TODO: what is an example of a country with more than 1 code?
    @Json(name = "iso-3166-1-codes") val iso_3166_1_codes: List<String>? = null,

//    @Json(name = "type_id")
//    override val typeId: String? = null,

    @Json(name = "life-span") override val lifeSpan: LifeSpan? = null,

    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Area
