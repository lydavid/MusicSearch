package ly.david.ui.stats

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.data.room.relation.RelationTypeCount
import ly.david.data.room.releasegroup.RoomReleaseGroupTypeCount

@Stable
data class Stats(
    val totalRelations: Int? = null,
    val relationTypeCounts: ImmutableList<RelationTypeCount> = persistentListOf(),
    val eventStats: EventStats = EventStats(),
    val placeStats: PlaceStats = PlaceStats(),
    val recordingStats: RecordingStats = RecordingStats(),
    val releaseStats: ReleaseStats = ReleaseStats(),
    val releaseGroupStats: ReleaseGroupStats = ReleaseGroupStats(),
)

data class EventStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

data class PlaceStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

data class RecordingStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

data class ReleaseStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
)

data class ReleaseGroupStats(
    val totalRemote: Int? = null,
    val totalLocal: Int = 0,
    val releaseGroupTypeCounts: ImmutableList<RoomReleaseGroupTypeCount> = persistentListOf(),
)
