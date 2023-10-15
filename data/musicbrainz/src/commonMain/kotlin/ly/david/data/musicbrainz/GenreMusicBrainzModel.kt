package ly.david.data.musicbrainz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.core.models.genre.Genre

@Serializable
data class GenreMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String? = null,
) : Genre, MusicBrainzModel()
