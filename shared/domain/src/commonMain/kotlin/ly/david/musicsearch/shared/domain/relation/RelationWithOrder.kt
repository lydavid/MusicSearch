package ly.david.musicsearch.shared.domain.relation

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

data class RelationWithOrder(
    val id: String,
    override val linkedEntityId: String,
    override val label: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val attributes: String? = null,
    override val linkedEntity: MusicBrainzEntityType,
    override val isForwardDirection: Boolean? = null,
    val order: Int,
    val lifeSpan: LifeSpanUiModel,
) : Relation
