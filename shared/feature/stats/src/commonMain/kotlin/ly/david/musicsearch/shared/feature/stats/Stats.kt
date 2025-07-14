package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.ui.common.topappbar.Tab

@Stable
internal data class Stats(
    val totalRelations: Int? = null,
    val relationTypeCounts: ImmutableList<RelationTypeCount> = persistentListOf(),
    val tabToStats: ImmutableMap<Tab, EntityStats> = persistentHashMapOf(),
)

internal data class EntityStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
    val totalVisited: Int? = null,
    val releaseGroupTypeCounts: ImmutableList<ReleaseGroupTypeCount> = persistentListOf(),
    val lastUpdated: Instant? = null,
)
