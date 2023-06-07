package ly.david.data.room.releasegroup.releases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

/**
 * This is a junction table linking releases and release groups.
 *
 * Note that a release actually belongs to a single release group.
 * However, this better allows us to represent paged releases by a release group.
 */
@Entity(
    tableName = "release_release_group",
    primaryKeys = ["release_id", "release_group_id"],
    indices = [Index(value = ["release_group_id"])]
)
data class ReleaseReleaseGroup(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "release_group_id")
    val releaseGroupId: String
)
