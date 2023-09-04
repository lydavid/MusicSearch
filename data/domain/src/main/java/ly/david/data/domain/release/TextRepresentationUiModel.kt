package ly.david.data.domain.release

import ly.david.data.core.TextRepresentation
import ly.david.data.musicbrainz.TextRepresentationMusicBrainzModel
import ly.david.data.room.release.TextRepresentationRoomModel

data class TextRepresentationUiModel(
    override val script: String?,
    override val language: String?,
) : TextRepresentation

fun TextRepresentationRoomModel.toTextRepresentationUiModel() = TextRepresentationUiModel(
    script = script,
    language = language,
)

fun TextRepresentationMusicBrainzModel.toTextRepresentationUiModel() = TextRepresentationUiModel(
    script = script,
    language = language,
)
