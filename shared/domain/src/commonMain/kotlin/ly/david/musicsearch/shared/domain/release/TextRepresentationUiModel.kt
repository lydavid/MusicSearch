package ly.david.musicsearch.shared.domain.release

data class TextRepresentationUiModel(
    override val script: String = "",
    override val language: String = "",
) : TextRepresentation
