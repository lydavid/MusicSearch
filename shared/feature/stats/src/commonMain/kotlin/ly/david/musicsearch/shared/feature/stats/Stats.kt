package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.relation.RelationStats
import ly.david.musicsearch.shared.domain.release.ReleaseStatusCount
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

@Stable
internal data class Stats(
    val relationStats: RelationStats = RelationStats(),
    val tabToStats: ImmutableMap<Tab, EntityStats> = persistentHashMapOf(),
)

@Stable
internal sealed interface EntityStats {
    val totalRemote: Int?
    val totalLocal: Int
    val totalVisited: Int
    val totalCollected: Int
    val lastUpdated: Instant?

    data class Default(
        override val totalRemote: Int? = null,
        override val totalLocal: Int = 0,
        override val totalVisited: Int = 0,
        override val totalCollected: Int = 0,
        override val lastUpdated: Instant? = null,
    ) : EntityStats

    data class ReleaseGroup(
        override val totalRemote: Int? = null,
        override val totalLocal: Int = 0,
        override val totalVisited: Int = 0,
        override val totalCollected: Int = 0,
        override val lastUpdated: Instant? = null,
        val typeCounts: ImmutableList<ReleaseGroupTypeCount> = persistentListOf(),
    ) : EntityStats

    data class Release(
        override val totalRemote: Int? = null,
        override val totalLocal: Int = 0,
        override val totalVisited: Int = 0,
        override val totalCollected: Int = 0,
        override val lastUpdated: Instant? = null,
        val statusCounts: ImmutableList<ReleaseStatusCount> = persistentListOf(),
    ) : EntityStats
}
