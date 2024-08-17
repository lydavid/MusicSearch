package ly.david.musicsearch.shared.domain.label

data class LabelWithCatalog(
    override val id: String,
    override val name: String,
    override val disambiguation: String?,
    override val type: String?,
    override val labelCode: Int?,
    val catalogNumber: String,
) : Label
