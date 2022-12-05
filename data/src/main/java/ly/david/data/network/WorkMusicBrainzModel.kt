package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Work

data class WorkMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "title") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,

    @Json(name = "type") override val type: String? = null,
//    @Json(name = "type-id") override val typeId: String? = null,

    // This is null when languages is empty.
    @Json(name = "language") override val language: String? = null,
//    @Json(name = "languages") override val languages: List<String>? = null,

    @Json(name = "iswcs") override val iswcs: List<String>? = null,
    @Json(name = "attributes") val attributes: List<WorkAttribute>? = null,

    // search API returns relations without target-type
    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null,
) : Work, MusicBrainzModel()

data class WorkAttribute(
    @Json(name = "type") val type: String,
    @Json(name = "type-id") val typeId: String,
    @Json(name = "value") val value: String
)
