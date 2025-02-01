package ly.david.musicsearch.shared.domain.release

data class TextRepresentationUiModel(
    override val script: String? = null,
    override val language: String? = null,
) : TextRepresentation
