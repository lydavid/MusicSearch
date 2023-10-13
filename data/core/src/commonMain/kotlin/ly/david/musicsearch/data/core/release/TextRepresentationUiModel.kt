package ly.david.musicsearch.data.core.release

import ly.david.musicsearch.data.core.TextRepresentation

data class TextRepresentationUiModel(
    override val script: String?,
    override val language: String?,
) : TextRepresentation
