package ly.david.data.room.release

import ly.david.data.core.TextRepresentation
import ly.david.data.musicbrainz.TextRepresentationMusicBrainzModel

data class TextRepresentationRoomModel(
    override val script: String?,
    override val language: String?,
) : TextRepresentation

fun TextRepresentationMusicBrainzModel.toTextRepresentationRoomModel() = TextRepresentationRoomModel(
    script = script,
    language = language,
)
