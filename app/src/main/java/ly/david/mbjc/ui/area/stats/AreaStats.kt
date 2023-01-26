package ly.david.mbjc.ui.area.stats

import ly.david.data.persistence.relation.RelationTypeCount

data class AreaStats(
    val totalLocalReleases: Int = 0,
    val totalRemoteReleases: Int? = null,
    val totalRelations: Int? = null,
    val relationTypeCounts: List<RelationTypeCount> = listOf()
)
