package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Artist
import ly.david.data.LifeSpan

data class ArtistMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String = "",
    @Json(name = "sort-name") override val sortName: String = "",
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "type") override val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "gender") override val gender: String? = null,
    @Json(name = "country") override val countryCode: String? = null,
    @Json(name = "life-span") override val lifeSpan: LifeSpan? = null,

    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Artist
