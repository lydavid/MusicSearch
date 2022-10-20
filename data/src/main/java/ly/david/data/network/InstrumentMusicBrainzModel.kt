package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Instrument

data class InstrumentMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String?,
    @Json(name = "description") override val description: String?,
    @Json(name = "type") override val type: String?,

    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Instrument
