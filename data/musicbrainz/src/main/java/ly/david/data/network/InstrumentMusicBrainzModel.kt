package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Instrument

data class InstrumentMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "description") override val description: String? = null,
    @Json(name = "type") override val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,

    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Instrument
