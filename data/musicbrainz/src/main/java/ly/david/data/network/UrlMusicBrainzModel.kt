package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Identifiable

data class UrlMusicBrainzModel(

    // Don't plan to store locally
    @Json(name = "id") override val id: String,

    /**
     * The url itself.
     */
    @Json(name = "resource") val resource: String
): Identifiable
