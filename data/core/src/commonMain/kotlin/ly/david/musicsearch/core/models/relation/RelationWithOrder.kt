package ly.david.musicsearch.core.models.relation

import ly.david.musicsearch.core.models.network.MusicBrainzEntity

data class RelationWithOrder(
    val id: String,
    override val linkedEntityId: String,
    override val label: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val attributes: String? = null,
    override val additionalInfo: String? = null,
    override val linkedEntity: MusicBrainzEntity,
    val order: Int,
) : Relation
