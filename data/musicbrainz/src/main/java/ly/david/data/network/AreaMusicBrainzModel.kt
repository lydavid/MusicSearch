package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Area
import ly.david.data.LifeSpan

data class AreaMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "sort-name") override val sortName: String = "",
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "type") override val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "life-span") override val lifeSpan: LifeSpan? = null,

    @Json(name = "iso-3166-1-codes") val countryCodes: List<String>? = null,
    @Json(name = "iso-3166-2-codes") val countrySubDivisionCodes: List<String>? = null,
    @Json(name = "iso-3166-3-codes") val formerCountryCodes: List<String>? = null,
    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Area
