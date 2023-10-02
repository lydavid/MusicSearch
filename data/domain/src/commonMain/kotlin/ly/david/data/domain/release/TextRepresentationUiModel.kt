package ly.david.data.domain.release

import ly.david.data.core.TextRepresentation
import ly.david.data.musicbrainz.TextRepresentationMusicBrainzModel

data class TextRepresentationUiModel(
    override val script: String?,
    override val language: String?,
) : TextRepresentation

fun TextRepresentationMusicBrainzModel.toTextRepresentationUiModel() = TextRepresentationUiModel(
    script = script,
    language = language,
)
