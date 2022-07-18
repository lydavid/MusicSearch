package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.LifeSpan

internal data class ArtistMusicBrainzModel(

    @Json(name = "id")
    override val id: String,

    @Json(name = "name")
    override val name: String = "",
    @Json(name = "sort-name")
    override val sortName: String = "",
    @Json(name = "disambiguation")
    override val disambiguation: String? = null,

    @Json(name = "type")
    override val type: String? = null,
//    @Json(name = "type-id")
//    val typeId: String? = null,

    @Json(name = "gender")
    override val gender: String? = null,
//    @Json(name = "gender-id")
//    val genderId: String? = null,

    @Json(name = "country")
    override val countryCode: String? = null,

    @Json(name = "life-span")
    override val lifeSpan: LifeSpan? = null,

    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null

    // for search responses only
//    @Json(name = "score")
//    val score: Int? = null,
): Artist, MusicBrainzModel()
