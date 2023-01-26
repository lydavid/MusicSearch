package ly.david.mbjc.ui.stats

import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount

data class Stats(
    val totalRelations: Int? = null,
    val relationTypeCounts: List<RelationTypeCount> = listOf(),

    val totalRemoteReleases: Int? = null,
    val totalLocalReleases: Int = 0,

    val totalRemoteReleaseGroups: Int? = null,
    val totalLocalReleaseGroups: Int = 0,
    val releaseGroupTypeCounts: List<ReleaseGroupTypeCount> = listOf(),

    val totalRemotePlaces: Int? = null,
    val totalLocalPlaces: Int = 0,
)
