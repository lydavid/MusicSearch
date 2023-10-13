package ly.david.musicsearch.data.core.relation

import ly.david.musicsearch.data.core.network.MusicBrainzEntity

data class RelationTypeCount(
    val linkedEntity: MusicBrainzEntity,
    val count: Int,
)
