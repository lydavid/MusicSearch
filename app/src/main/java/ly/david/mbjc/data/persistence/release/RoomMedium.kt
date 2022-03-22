package ly.david.mbjc.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.Medium
import ly.david.mbjc.data.network.MusicBrainzMedium
import ly.david.mbjc.data.persistence.BaseDao
import ly.david.mbjc.data.persistence.RoomRelease

@Dao
abstract class MediumDao : BaseDao<RoomMedium> {

    @Transaction
    @Query("""
        SELECT m.*
        FROM media m
        INNER JOIN tracks t ON t.medium_id = m.id
        WHERE t.id = :trackId
    """)
    abstract suspend fun getMediumForTrack(trackId: String): RoomMedium
}

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
