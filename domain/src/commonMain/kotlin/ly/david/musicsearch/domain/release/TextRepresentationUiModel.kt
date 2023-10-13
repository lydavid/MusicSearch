package ly.david.musicsearch.domain.release

import ly.david.musicsearch.data.core.TextRepresentation
import ly.david.data.musicbrainz.TextRepresentationMusicBrainzModel

data class TextRepresentationUiModel(
    override val script: String?,
    override val language: String?,
) : TextRepresentation

fun TextRepresentationMusicBrainzModel.toTextRepresentationUiModel() = TextRepresentationUiModel(
    script = script,
    language = language,
)
