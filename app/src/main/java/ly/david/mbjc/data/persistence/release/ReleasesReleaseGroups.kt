package ly.david.mbjc.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "releases_release_groups",
    primaryKeys = ["release_id", "release_group_id"]
)
internal data class ReleasesReleaseGroups(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "release_group_id")
    val releaseGroupId: String,
)
