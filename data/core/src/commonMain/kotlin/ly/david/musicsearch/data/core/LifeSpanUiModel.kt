package ly.david.musicsearch.data.core

data class LifeSpanUiModel(
    override val begin: String? = null,
    override val end: String? = null,
    override val ended: Boolean? = null,
) : LifeSpan
