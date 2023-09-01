package ly.david.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.data.Identifiable

@Serializable
data class UrlMusicBrainzModel(

    // Don't plan to store locally
    @SerialName("id") override val id: String,

    /**
     * The url itself.
     */
    @SerialName("resource") val resource: String,
) : Identifiable
