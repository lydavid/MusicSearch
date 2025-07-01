package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.work.Work
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel

data class WorkListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val language: String? = null,
    override val iswcs: List<String>? = null,
    val attributes: List<WorkAttributeUiModel> = listOf(),
    override val visited: Boolean = false,
    override val collected: Boolean = false,
) : EntityListItemModel, Work
