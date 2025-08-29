package ly.david.musicsearch.shared.domain

data class LifeSpanUiModel(
    override val begin: String = "",
    override val end: String = "",
    override val ended: Boolean = false,
) : LifeSpan
