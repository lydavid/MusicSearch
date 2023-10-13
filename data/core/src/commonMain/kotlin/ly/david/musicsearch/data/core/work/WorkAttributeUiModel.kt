package ly.david.musicsearch.data.core.work

import ly.david.musicsearch.data.core.WorkAttribute

data class WorkAttributeUiModel(
    override val type: String,
    override val typeId: String,
    override val value: String,
) : WorkAttribute
