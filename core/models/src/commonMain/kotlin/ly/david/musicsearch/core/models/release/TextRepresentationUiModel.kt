package ly.david.musicsearch.core.models.release

data class TextRepresentationUiModel(
    override val script: String?,
    override val language: String?,
) : TextRepresentation
