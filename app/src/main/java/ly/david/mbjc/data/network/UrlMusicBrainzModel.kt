package ly.david.mbjc.data.network

import com.squareup.moshi.Json

internal data class UrlMusicBrainzModel(

    // Don't plan to store locally
    @Json(name = "id") val id: String,

    /**
     * The url itself.
     */
    @Json(name = "resource") val resource: String
)
