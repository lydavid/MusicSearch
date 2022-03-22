package ly.david.mbjc.data.persistence.release

import androidx.paging.PagingSource
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.Track
import ly.david.mbjc.data.network.MusicBrainzTrack
import ly.david.mbjc.data.persistence.BaseDao

@Dao
abstract class TrackDao : BaseDao<RoomTrack> {

    @Transaction
    @Query(
        """
        SELECT t.*
        FROM tracks t
        INNER JOIN media m ON t.medium_id = m.id
        INNER JOIN releases r ON m.release_id = r.id
        WHERE r.id = :releaseId
    """
    )
    abstract fun getTracksInRelease(releaseId: String): PagingSource<Int, RoomTrack>
}

// TODO: check that deleting a release will delete all media and tracks
@Entity(
    tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = RoomMedium::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("medium_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RoomTrack(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "medium_id")
    val mediumId: Long,

    @ColumnInfo(name = "position")
    override val position: Int,
    @ColumnInfo(name = "number")
    override val number: String,
    @ColumnInfo(name = "title")
    override val title: String,
    @ColumnInfo(name = "length")
    override val length: Int?
) : Track

fun MusicBrainzTrack.toRoomTrack(mediumId: Long) =
    RoomTrack(
        id = id,
        mediumId = mediumId,
        position = position,
        number = number,
        title = title,
        length = length
    )
