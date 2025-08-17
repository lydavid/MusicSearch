package ly.david.musicsearch.shared.domain.relation

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

data class RelationTypeCount(
    val linkedEntity: MusicBrainzEntityType,
    val count: Int,
)
