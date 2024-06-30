package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.work.Work
import ly.david.musicsearch.core.models.work.WorkAttributeUiModel

data class WorkListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val language: String? = null,
    override val iswcs: List<String>? = null,
    val attributes: List<WorkAttributeUiModel> = listOf(),
) : Work, ListItemModel()
