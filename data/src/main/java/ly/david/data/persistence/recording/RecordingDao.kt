package ly.david.data.persistence.recording

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao

@Dao
abstract class RecordingDao : BaseDao<RecordingRoomModel> {

    @Query("SELECT * FROM recordings WHERE id = :recordingId")
    abstract suspend fun getRecording(recordingId: String): RecordingRoomModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllArtistCredits(artistCredits: List<RecordingArtistCreditRoomModel>)

    @Transaction
    @Query(
        """
        SELECT ra.*
        FROM recordings r
        INNER JOIN recordings_artists ra ON r.id = ra.recording_id
        where r.id = :recordingId
        ORDER BY ra.`order`
    """
    )
    abstract suspend fun getRecordingArtistCredits(recordingId: String): List<RecordingArtistCreditRoomModel>
}
