package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Event
import ly.david.data.LifeSpan

data class EventMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "type") override val type: String? = null,
    @Json(name = "life-span") override val lifeSpan: LifeSpan? = null,

    // TODO: time, cancelled

    // search API returns relations without target-type
    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null,
) : Event, MusicBrainzModel()
