package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Genre

internal data class GenreMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String?,
) : Genre, MusicBrainzModel()
