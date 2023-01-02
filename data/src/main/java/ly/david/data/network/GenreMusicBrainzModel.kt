package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Genre

data class GenreMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
) : Genre, MusicBrainzModel()
