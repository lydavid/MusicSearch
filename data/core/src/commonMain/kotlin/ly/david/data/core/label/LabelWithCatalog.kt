package ly.david.data.core.label

import ly.david.data.core.Label

data class LabelWithCatalog(
    override val id: String,
    override val name: String,
    override val disambiguation: String?,
    override val type: String?,
    override val labelCode: Int?,
    val catalogNumber: String,
) : Label
