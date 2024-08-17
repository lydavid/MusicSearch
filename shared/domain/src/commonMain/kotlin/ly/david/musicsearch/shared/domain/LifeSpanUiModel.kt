package ly.david.musicsearch.shared.domain

data class LifeSpanUiModel(
    override val begin: String? = null,
    override val end: String? = null,
    override val ended: Boolean? = null,
) : LifeSpan
