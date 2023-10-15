package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.work.Work
import ly.david.musicsearch.core.models.work.WorkAttributeUiModel

// TODO: map "qaa" to Artificial (Other), and rest from 3 letter code to full language name
data class WorkListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val language: String? = null,
    override val iswcs: List<String>? = null,
    val attributes: List<WorkAttributeUiModel> = listOf(),
) : Work, ListItemModel()
