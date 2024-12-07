package ly.david.musicsearch.shared.domain.relation

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

data class RelationWithOrder(
    val id: String,
    override val linkedEntityId: String,
    override val label: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val attributes: String? = null,
    override val additionalInfo: String? = null,
    override val linkedEntity: MusicBrainzEntity,
    override val isForwardDirection: Boolean? = null,
    val order: Int,
    val lifeSpan: LifeSpanUiModel,
) : Relation
