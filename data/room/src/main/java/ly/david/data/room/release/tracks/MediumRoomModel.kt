package ly.david.data.room.release.tracks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ly.david.data.core.Medium
import ly.david.data.network.MediumMusicBrainzModel
import ly.david.data.room.release.ReleaseRoomModel

@Entity(
    tableName = "medium",
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
data class MediumRoomModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "release_id", index = true) val releaseId: String,
    @ColumnInfo(name = "position") override val position: Int?,
    @ColumnInfo(name = "title") override val title: String?,
    @ColumnInfo(name = "track_count") override val trackCount: Int,
    @ColumnInfo(name = "format") override val format: String? = null,
    @ColumnInfo(name = "format_id") val formatId: String? = null,
) : Medium

fun MediumMusicBrainzModel.toMediumRoomModel(releaseId: String) =
    MediumRoomModel(
        releaseId = releaseId,
        position = position,
        title = title,
        trackCount = trackCount,
        format = format,
        formatId = formatId
    )
