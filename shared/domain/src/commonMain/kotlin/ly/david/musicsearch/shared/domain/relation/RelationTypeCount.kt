package ly.david.musicsearch.shared.domain.relation

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

data class RelationTypeCount(
    val linkedEntity: MusicBrainzEntity,
    val count: Int,
)
