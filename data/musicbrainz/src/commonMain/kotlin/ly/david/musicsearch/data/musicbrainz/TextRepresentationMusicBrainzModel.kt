package ly.david.musicsearch.data.musicbrainz

import kotlinx.serialization.Serializable
import ly.david.musicsearch.core.models.release.TextRepresentation

@Serializable
data class TextRepresentationMusicBrainzModel(
    override val script: String? = null,
    override val language: String? = null,
) : TextRepresentation
