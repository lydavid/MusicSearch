package ly.david.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.data.Recording

@Serializable
data class RecordingMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("title") override val name: String,
    @SerialName("first-release-date") override val firstReleaseDate: String? = null,
    @SerialName("disambiguation") override val disambiguation: String = "",
    @SerialName("length") override val length: Int? = null,
    @SerialName("video") override val video: Boolean? = false,
    @SerialName("isrcs") val isrcs: List<String>? = null,

    @SerialName("artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,
    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), Recording
