package ly.david.musicsearch.shared.domain.relation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant

data class RelationStats(
    val relationTypeCounts: ImmutableList<RelationTypeCount> = persistentListOf(),
    val lastUpdated: Instant? = null,
)
