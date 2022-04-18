package ly.david.mbjc.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Medium
import ly.david.mbjc.data.network.MediumMusicBrainzModel
import ly.david.mbjc.data.persistence.ReleaseRoomModel

// TODO: release_id column references a foreign key but it is not part of an index. This may trigger full table scans whenever parent table is modified so you are highly advised to create an index that covers this column. - ly.david.mbjc.data.persistence.release.MediumRoomModel
@Entity(
    tableName = "media",
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
internal data class MediumRoomModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "release_id")
    val releaseId: String,
    @ColumnInfo(name = "position")
    override val position: Int,
    @ColumnInfo(name = "title")
    override val title: String,
    @ColumnInfo(name = "track_count")
    override val trackCount: Int,
    @ColumnInfo(name = "format")
    override val format: String? = null,
) : Medium

internal fun MediumMusicBrainzModel.toMediumRoomModel(releaseId: String) =
    MediumRoomModel(
        releaseId = releaseId,
        position = position,
        title = title,
        trackCount = trackCount,
        format = format
    )
