package ly.david.data.persistence.recording

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class RecordingDao : BaseDao<RecordingRoomModel> {

    @Query("SELECT * FROM recordings WHERE id = :recordingId")
    abstract suspend fun getRecording(recordingId: String): RecordingRoomModel?
}
