package ly.david.mbjc.data

import com.squareup.moshi.Json
import ly.david.mbjc.data.network.RelationMusicBrainzModel

internal data class Work(
    @Json(name = "id") val id: String,
    @Json(name = "title") override val name: String,

    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,

    @Json(name = "disambiguation") override val disambiguation: String? = null,

    @Json(name = "language") val language: String? = null,
    @Json(name = "languages") val languages: List<String>? = null,

    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null,
): NameWithDisambiguation
