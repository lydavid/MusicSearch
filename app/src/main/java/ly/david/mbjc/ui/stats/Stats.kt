package ly.david.mbjc.ui.stats

import ly.david.data.room.relation.RelationTypeCount
import ly.david.data.room.releasegroup.ReleaseGroupTypeCount

data class Stats(
    val totalRelations: Int? = null,
    val relationTypeCounts: List<RelationTypeCount> = listOf(),

    val totalRemoteEvents: Int? = null,
    val totalLocalEvents: Int = 0,

    val totalRemotePlaces: Int? = null,
    val totalLocalPlaces: Int = 0,

    val totalRemoteRecordings: Int? = null,
    val totalLocalRecordings: Int = 0,

    val totalRemoteReleases: Int? = null,
    val totalLocalReleases: Int = 0,

    val totalRemoteReleaseGroups: Int? = null,
    val totalLocalReleaseGroups: Int = 0,
    val releaseGroupTypeCounts: List<ReleaseGroupTypeCount> = listOf(),
)
