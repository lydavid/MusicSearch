package ly.david.mbjc.data.persistence.recording

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class RecordingRelationDao : BaseDao<RecordingRelationRoomModel> {

    @Query(
        """
            SELECT rr.*
            FROM recordings_relations rr
            INNER JOIN recordings rec ON rr.recording_id = rec.id
            WHERE rec.id = :recordingId
            ORDER BY rr.`order`
        """
    )
    abstract fun getRelationsForRecording(recordingId: String): PagingSource<Int, RecordingRelationRoomModel>
}
