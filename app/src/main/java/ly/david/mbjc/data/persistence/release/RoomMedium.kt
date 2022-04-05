package ly.david.mbjc.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Medium
import ly.david.mbjc.data.network.MusicBrainzMedium
import ly.david.mbjc.data.persistence.RoomRelease

@Entity(
    tableName = "media",
    foreignKeys = [
        ForeignKey(
            entity = RoomRelease::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("release_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RoomMedium(
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

fun MusicBrainzMedium.toRoomMedium(releaseId: String) =
    RoomMedium(
        releaseId = releaseId,
        position = position,
        title = title,
        trackCount = trackCount,
        format = format
    )
