package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.models.relation.RelationTypeCount
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupTypeCount

@Stable
internal data class Stats(
    val totalRelations: Int? = null,
    val relationTypeCounts: ImmutableList<RelationTypeCount> = persistentListOf(),
    val artistStats: ArtistStats = ArtistStats(),
    val eventStats: EventStats = EventStats(),
    val placeStats: PlaceStats = PlaceStats(),
    val recordingStats: RecordingStats = RecordingStats(),
    val releaseStats: ReleaseStats = ReleaseStats(),
    val releaseGroupStats: ReleaseGroupStats = ReleaseGroupStats(),
    val workStats: WorkStats = WorkStats(),
)

internal data class ArtistStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

internal data class EventStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

internal data class PlaceStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

internal data class RecordingStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

internal data class ReleaseStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

internal data class ReleaseGroupStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
    val releaseGroupTypeCounts: ImmutableList<ReleaseGroupTypeCount> = persistentListOf(),
)

internal data class WorkStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)
