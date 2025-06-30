package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.label.Label

data class LabelListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,

    val catalogNumbers: String? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
) : EntityListItemModel, Label
