package ly.david.data.room.release

import ly.david.data.core.TextRepresentation

data class TextRepresentationRoomModel(
    override val script: String?,
    override val language: String?,
) : TextRepresentation
