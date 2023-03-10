package ly.david.data.network

import com.squareup.moshi.Json

data class CollectionMusicBrainzModel(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "editor") val editor: String,
    @Json(name = "type") val type: String? = null,
    @Json(name = "entity-type") val entityType: MusicBrainzResource,

    // TODO:
    @Json(name = "artist-count") val artistCount: Int? = null,
    @Json(name = "release-count") val releaseCount: Int? = null,
)
