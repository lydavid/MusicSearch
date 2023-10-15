package ly.david.musicsearch.core.models.relation

import ly.david.musicsearch.core.models.network.MusicBrainzEntity

data class RelationTypeCount(
    val linkedEntity: MusicBrainzEntity,
    val count: Int,
)
