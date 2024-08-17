package ly.david.musicsearch.shared.domain.work

data class WorkAttributeUiModel(
    override val type: String,
    override val typeId: String,
    override val value: String,
) : WorkAttribute
