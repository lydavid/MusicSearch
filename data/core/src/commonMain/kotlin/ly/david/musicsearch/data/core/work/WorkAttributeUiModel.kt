package ly.david.musicsearch.data.core.work

data class WorkAttributeUiModel(
    override val type: String,
    override val typeId: String,
    override val value: String,
) : WorkAttribute
