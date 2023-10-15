package ly.david.data.musicbrainz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.core.models.Identifiable

@Serializable
data class UrlMusicBrainzModel(

    // Don't plan to store locally
    @SerialName("id") override val id: String,

    /**
     * The url itself.
     */
    @SerialName("resource") val resource: String,
) : Identifiable
