package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Instrument

internal data class InstrumentMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String?,
    @Json(name = "description") override val description: String?,
    @Json(name = "type") override val type: String?,
) : MusicBrainzModel(), Instrument
