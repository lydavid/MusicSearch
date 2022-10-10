package ly.david.mbjc.data.persistence.releasegroup

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel

/**
 * Links a release with a release group.
 *
 * - When a release is deleted, delete this link
 * - When a release group is deleted, we don't have to delete this since
 * we use it for up navigation from the release
 */
@Entity(
    tableName = "releases_release_groups",
    primaryKeys = ["release_id", "release_group_id"],
    foreignKeys = [
        ForeignKey(
            entity = ReleaseRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("release_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class ReleasesReleaseGroups(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "release_group_id")
    val releaseGroupId: String,
)
