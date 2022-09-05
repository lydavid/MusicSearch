package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Series

internal data class SeriesMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,

    @Json(name = "type") override val type: String? = null,
//    @Json(name = "type-id") override val typeId: String? = null,

    // search API returns relations without target-type
//    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null,
) : Series, MusicBrainzModel()
