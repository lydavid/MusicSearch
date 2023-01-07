package ly.david.mbjc.ui.artist.stats

import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount

data class ArtistStats(
    val totalRemoteReleaseGroups: Int?,
    val totalLocalReleaseGroups: Int,
    val releaseGroupTypeCounts: List<ReleaseGroupTypeCount>,
    val totalLocalReleases: Int,
    val totalRemoteReleases: Int,
    val totalRelations: Int?,
    val relationTypeCounts: List<RelationTypeCount>
)
