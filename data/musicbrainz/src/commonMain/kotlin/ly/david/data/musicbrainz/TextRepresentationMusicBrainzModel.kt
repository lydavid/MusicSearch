package ly.david.data.musicbrainz

import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.core.release.TextRepresentation

@Serializable
data class TextRepresentationMusicBrainzModel(
    override val script: String? = null,
    override val language: String? = null,
) : TextRepresentation
