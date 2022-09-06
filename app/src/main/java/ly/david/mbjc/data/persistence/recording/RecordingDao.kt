package ly.david.mbjc.data.persistence.recording

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class RecordingDao : BaseDao<RecordingRoomModel> {

    @Query("SELECT * FROM recordings WHERE id = :recordingId")
    abstract suspend fun getRecording(recordingId: String): RecordingRoomModel?
}
