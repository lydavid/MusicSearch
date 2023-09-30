package ly.david.data.core.relation

import ly.david.data.core.network.MusicBrainzEntity

data class RelationTypeCount(
    val linkedEntity: MusicBrainzEntity,
    val count: Int,
)
